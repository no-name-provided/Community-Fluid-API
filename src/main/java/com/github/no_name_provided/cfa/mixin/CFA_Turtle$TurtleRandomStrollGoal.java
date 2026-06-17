package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtleRandomStrollGoal")
abstract class CFA_Turtle$TurtleRandomStrollGoal extends RandomStrollGoal {
    
    public CFA_Turtle$TurtleRandomStrollGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isInWater()Z"))
    private boolean cfa_canUse(boolean original) {
        return !mob.canSwimInFluidType(mob.getLastFluid());
    }
}
