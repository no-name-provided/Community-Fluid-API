package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.TaggedFluidType;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
abstract class Fun_Fluids_LocalPlayer extends AbstractClientPlayer {
    
    @Shadow
    public abstract boolean isUnderWater();
    
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
//                        type.canSwim(this) &&
                                type instanceof TaggedFluidType tagged &&
                                        this.fluidInteraction.isInFluid(tagged.getTag())
                );
    }
    
    /**
     * Don't stop swimsprinting if we're in one of our fluids.
     */
    @ModifyExpressionValue(method = "shouldStopSwimSprinting()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    private boolean Fun_Fluids_shouldStopSwimSprinting(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                );
    }
    
    /**
     *
     */
    @ModifyExpressionValue(method = "isSprintingPossible(Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInShallowWater()Z"))
    private boolean Fun_Fluids_isSprintingPossible_FixShallowWaterCheck(boolean original) {
        // Find out which fluid we're actually in
        FluidType fluidType = NeoForgeRegistries.FLUID_TYPES.stream()
                .filter(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                ).findFirst()
                // But default to the vanilla check
                .orElse(NeoForgeMod.WATER_TYPE.value());
        // If it's one of ours, we do a separate check
        if (fluidType instanceof TaggedFluidType taggedFluidType) {
            return fluidInteraction.isInFluid(taggedFluidType.getTag()) && !fluidInteraction.isEyeInFluid(taggedFluidType.getTag());
        } else {
            return original;
        }
    }
}
