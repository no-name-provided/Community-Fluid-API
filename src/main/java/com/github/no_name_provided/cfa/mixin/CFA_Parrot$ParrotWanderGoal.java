package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.parrot.Parrot.ParrotWanderGoal")
abstract class CFA_Parrot$ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
    
    public CFA_Parrot$ParrotWanderGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }
    
    @ModifyExpressionValue(method = "getPosition()Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isInWater()Z"))
    private boolean cfa_doTick(boolean original) {
        return original || mob.canDrownInFluidType(mob.getLastFluid());
    }
}
