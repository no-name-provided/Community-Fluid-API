package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeatherEffectRenderer.class)
abstract class CFA_WeatherEffectRenderer {
    
    /**
     * If a fluid is at least as hot as lava, it should probably turn rain into "smoke". Lava is 1300 units hot, as of
     * Neo 26.1.2.73.
     */
    @ModifyExpressionValue(method = "tickRainParticles(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;ILnet/minecraft/server/level/ParticleStatus;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tickRainParticles_fixLavaCheck(boolean original, @Local(name = "fluid") FluidState fluid) {
        return original || fluid.getFluidType().getTemperature() >= NeoForgeMod.LAVA_TYPE.value().getTemperature();
    }
}
