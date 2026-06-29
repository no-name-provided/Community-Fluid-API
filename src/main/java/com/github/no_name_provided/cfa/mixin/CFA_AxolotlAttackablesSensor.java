package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.AxolotlAttackablesSensor;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxolotlAttackablesSensor.class)
abstract class CFA_AxolotlAttackablesSensor extends NearestVisibleLivingEntitySensor {
    
    /**
     * Lets axolotl attack living entities in modded fluids.
     */
    @ModifyExpressionValue(method = "isMatchingEntity(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private boolean cfa_isMatchingEntity(boolean original, ServerLevel level, LivingEntity body, LivingEntity mob) {
        return original || body.canSwimInFluidType(mob.getLastFluid());
    }
}
