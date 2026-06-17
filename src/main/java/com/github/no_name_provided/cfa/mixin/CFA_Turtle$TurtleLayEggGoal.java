package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.turtle.Turtle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtleLayEggGoal")
abstract class CFA_Turtle$TurtleLayEggGoal extends MoveToBlockGoal {
    
    @Shadow @Final
    private Turtle turtle;
    
    public CFA_Turtle$TurtleLayEggGoal(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_getWalkTargetValue(boolean original) {
        return original || turtle.canSwimInFluidType(turtle.getLastFluid());
    }
}
