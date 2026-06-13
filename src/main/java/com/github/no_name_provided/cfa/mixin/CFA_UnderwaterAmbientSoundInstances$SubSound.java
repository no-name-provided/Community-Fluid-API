package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(UnderwaterAmbientSoundInstances.SubSound.class)
abstract class CFA_UnderwaterAmbientSoundInstances$SubSound extends AbstractTickableSoundInstance {
    @Shadow @Final
    private LocalPlayer player;
    
    protected CFA_UnderwaterAmbientSoundInstances$SubSound(SoundEvent event, SoundSource source, RandomSource random) {
        super(event, source, random);
    }
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || ((IFluidTypeExtension)player.getLastFluid()).hasUnderWaterMusic();
    }
}
