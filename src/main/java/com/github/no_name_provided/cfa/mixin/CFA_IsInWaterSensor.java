package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.IsInWaterSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IsInWaterSensor.class)
abstract class CFA_IsInWaterSensor extends Sensor<LivingEntity> {
    
    @ModifyExpressionValue(method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private boolean cfa_doTick(boolean original, @Local(name = "body", argsOnly = true) LivingEntity body) {
        return original || body.canSwimInFluidType(body.getLastFluid());
    }
}
