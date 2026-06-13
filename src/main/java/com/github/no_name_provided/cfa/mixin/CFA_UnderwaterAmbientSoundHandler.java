package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(UnderwaterAmbientSoundHandler.class)
abstract class CFA_UnderwaterAmbientSoundHandler implements AmbientSoundHandler {
    
    @Shadow @Final
    private LocalPlayer player;
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || player.getWasUnderLastFluid();
    }
}
