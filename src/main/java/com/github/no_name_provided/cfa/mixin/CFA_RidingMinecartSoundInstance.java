package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.resources.sounds.RidingEntitySoundInstance;
import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RidingMinecartSoundInstance.class)
abstract class CFA_RidingMinecartSoundInstance extends RidingEntitySoundInstance {
    @Shadow @Final
    private Player player;
    
    public CFA_RidingMinecartSoundInstance(Player player, Entity entity, boolean underwaterSound, SoundEvent soundEvent, SoundSource soundSource, float volumeMin, float volumeMax, float volumeAmplifier) {
        super(player, entity, underwaterSound, soundEvent, soundSource, volumeMin, volumeMax, volumeAmplifier);
    }
    
    @ModifyExpressionValue(method = "shouldNotPlayUnderwaterSound()Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isUnderWater()Z"))
    private boolean cfa_shouldNotPlayUnderwaterSound(boolean original) {
        return original || player.getWasUnderLastFluid();
    }
}
