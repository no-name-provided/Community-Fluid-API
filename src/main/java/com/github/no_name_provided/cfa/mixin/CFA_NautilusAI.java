package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.nautilus.NautilusAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Just matching the stuff I'm mixing into
@Mixin(NautilusAi.class)
abstract class CFA_NautilusAI {
    
    @ModifyExpressionValue(method = "findNearestValidAttackTarget(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;)Ljava/util/Optional;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;isInWater()Z"))
    private static boolean cfa_findNearestValidAttackTarget_fixIsInWater(boolean original, ServerLevel level, AbstractNautilus body) {
        return original || body.canSwimInFluidType(body.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "findNearestValidAttackTarget(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;)Ljava/util/Optional;",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"))
    private static Optional<LivingEntity> cfa_findNearestValidAttackTarget_fixFilter(Optional<LivingEntity> original, ServerLevel level, AbstractNautilus body) {
        return original.isPresent() ? original : BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT)
                .filter(entity -> entity.getLastFluid().canSwim(null) && Sensor.isEntityAttackableIgnoringLineOfSight(level, body, entity));
    }
    
    /**
     * We bypass the one and only call to isHostileTarget here so we can use our nautilus entity while checking swimming
     * conditions.
     */
    @ModifyArg(method = "findNearestValidAttackTarget(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;)Ljava/util/Optional;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/memory/NearestVisibleLivingEntities;findClosest(Ljava/util/function/Predicate;)Ljava/util/Optional;"))
    private static Predicate<LivingEntity> cfa_findNearestValidAttackTarget(Predicate<LivingEntity> filter, @Local(argsOnly = true, name = "body") AbstractNautilus body) {
        return mob -> body.canSwimInFluidType(mob.getLastFluid()) && mob.is(EntityTypeTags.NAUTILUS_HOSTILES);
    }
    
    /**
     * Because this method doesn't have a valid entity reference, we should prefer redirecting calls to it.
     */
    @ModifyExpressionValue(method = "isHostileTarget(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private static boolean cfa_isHostileTarget(boolean original, LivingEntity mob) {
        return original || mob.getLastFluid().canSwim(null);
    }
}
