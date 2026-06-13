package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
abstract class CFA_LocalPlayer extends AbstractClientPlayer {
    @Shadow @Final
    protected Minecraft minecraft;
    
    public CFA_LocalPlayer(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }
    
    /**
     * Trigger sinking while shifting in modded fluids.
     */
    @ModifyExpressionValue(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    private boolean cfa_aiStep(boolean original) {
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
    private boolean cfa_shouldStopSwimSprinting(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim(this) &&
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
    private boolean cfa_canStartSprinting_fixUnderWaterCheck(boolean original) {
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
    private boolean cfa_isSprintingPossible_FixShallowWaterCheck(boolean original) {
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
    
    /**
     * @author No Name Provided.
     * @reason Was getting some weird results with my ModifyVariable mixins. This allows underwater music to play in
     * modded fluids.
     */
    @Overwrite
    protected boolean updateIsUnderwater() {
        boolean oldIsUnderwater = getWasUnderLastFluid();
        setLastFluid(
                NeoForgeRegistries.FLUID_TYPES.stream().filter(type ->
                        fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                ).findFirst().orElse(NeoForgeMod.EMPTY_TYPE.value())
        );
        setWasUnderLastFluid(((IFluidTypeExtension) getLastFluid()).hasUnderWaterMusic() && isEyeInFluid(((IFluidTypeExtension) getLastFluid()).getTag()));
        super.updateIsUnderwater();
        boolean newIsUnderwater = getWasUnderLastFluid();
        if (!this.isSpectator()) {
            if (!oldIsUnderwater && newIsUnderwater) {
                this.level()
                        .playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.AMBIENT, 1.0F, 1.0F, false);
                this.minecraft.getSoundManager().play(new UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance((LocalPlayer) (Object) this));
            }
            
            if (oldIsUnderwater && !newIsUnderwater) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }
            
        }
        return this.wasUnderwater;
    }

//    @ModifyVariable(method = "updateIsUnderwater()Z",
//            at = @At(value = "LOAD"),
//            name = "oldIsUnderwater")
//    private boolean cfa_updateIsUnderwater_fixOldIsUnderWater(boolean oldIsUnderwater) {
//        return oldIsUnderwater ||
//                (
//                        ((IFluidTypeExtension) getLastFluid()).hasUnderWaterMusic() &&
//                                fluidInteraction.isEyeInFluid(((IFluidTypeExtension) getLastFluid()).getTag())
//                );
//    }
//
//    @ModifyVariable(method = "updateIsUnderwater()Z",
//            at = @At(value = "LOAD"),
//            name = "newIsUnderwater")
//    private boolean cfa_updateIsUnderwater_fixNewIsUnderWater(boolean newIsUnderwater) {
//        // Make sure we're using the new fluid
//        IFluidTypeExtension newFluid = (IFluidTypeExtension) NeoForgeRegistries.FLUID_TYPES.stream().filter(type ->
//                ((IFluidTypeExtension) type).hasUnderWaterMusic() &&
//                        fluidInteraction.isEyeInFluid(((IFluidTypeExtension) type).getTag())
//        ).findFirst().orElse(NeoForgeMod.EMPTY_TYPE.value());
//        return newIsUnderwater ||
//                (
//                        newFluid.hasUnderWaterMusic() &&
//                                fluidInteraction.isEyeInFluid(newFluid.getTag())
//                );
//    }
}
