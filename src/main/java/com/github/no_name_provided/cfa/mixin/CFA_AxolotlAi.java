package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.AxolotlAi;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(AxolotlAi.class)
abstract class CFA_AxolotlAi {
    
    /**
     * Replace the predicate with one that allows modded fluids.
     */
    @ModifyArg(method = "initIdleActivity()Lnet/minecraft/world/entity/ai/ActivityData;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/declarative/BehaviorBuilder;triggerIf(Ljava/util/function/Predicate;)Lnet/minecraft/world/entity/ai/behavior/OneShot;", ordinal = 0))
    private static Predicate<? extends LivingEntity> cfa_initIdleActivity(Predicate<? extends LivingEntity> original) {
        return (livingEntity) -> livingEntity.canSwimInFluidType(livingEntity.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "canSetWalkTargetFromLookTarget(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isWaterAt(Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean cfa_canSetWalkTargetFromLookTarget_fixIsWaterAt(boolean original, LivingEntity body, @Local(name = "level") Level level, @Local(name = "pos") BlockPos pos) {
        return original || body.canSwimInFluidType(level.getFluidState(pos).getFluidType());
    }
    
    @ModifyExpressionValue(method = "canSetWalkTargetFromLookTarget(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isWaterAt(Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean cfa_canSetWalkTargetFromLookTarget_fixIsInWater(boolean original, LivingEntity body, @Local(name = "level") Level level, @Local(name = "pos") BlockPos pos) {
        return original || body.canSwimInFluidType(body.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "getSpeedModifierChasing(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private static boolean cfa_getSpeedModifierChasing(boolean original, LivingEntity mob) {
        return original || mob.canSwimInFluidType(mob.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "getSpeedModifierFollowingAdult(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private static boolean cfa_getSpeedModifierFollowingAdult(boolean original, LivingEntity mob) {
        return original || mob.canSwimInFluidType(mob.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "getSpeedModifier(Lnet/minecraft/world/entity/LivingEntity;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private static boolean cfa_getSpeedModifier(boolean original, LivingEntity mob) {
        return original || mob.canSwimInFluidType(mob.getLastFluid());
    }
}
