package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
abstract class Fun_Fluids_LocalPlayer extends AbstractClientPlayer {
    
    public Fun_Fluids_LocalPlayer(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }
    
    /**
     * Trigger sinking while shifting in modded fluids.
     */
    @ModifyExpressionValue(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    private boolean isInWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * Don't stop swimsprinting if we're in one of our fluids and can swim.
     */
    @ModifyExpressionValue(method = "shouldStopSwimSprinting()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    private boolean Fun_Fluids_shouldStopSwimSprinting(boolean original) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim(player) &&
                                !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * Extends underwater check to apply to all fluids that can be swum in.
     * <p>
     * Note: in Minecraft's internal parlance, "swim" means "swimsprint".
     * </p>
     * <p>
     * Currently has no effect, since this isn't where vanilla stops players from swimming in invalid fluids.
     * </p>
     */
    @ModifyExpressionValue(method = "canStartSprinting()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    private boolean Fun_Fluids_canStartSprinting_fixUnderWaterCheck(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim(this) &&
                                !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * Check to see if the player is standing in any shallow fluid. Well, any modded one or water.
     */
    @ModifyExpressionValue(method = "isSprintingPossible(Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInShallowWater()Z"))
    private boolean Fun_Fluids_isSprintingPossible_FixShallowWaterCheck(boolean original) {
        // Find out which fluid we're actually in
        FluidType fluidType = NeoForgeRegistries.FLUID_TYPES.stream()
                .filter(type ->
                        !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                ).findFirst()
                // But default to the vanilla check
                .orElse(NeoForgeMod.WATER_TYPE.value());
        // If it's modded, we do a separate check
        if (!fluidType.isVanilla()) {
            return fluidInteraction.isInFluid(((IFluidTypeExtension) fluidType).getTag()) && !fluidInteraction.isEyeInFluid(((IFluidTypeExtension) fluidType).getTag());
        } else {
            return original;
        }
    }
}
