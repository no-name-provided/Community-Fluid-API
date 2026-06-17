package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtleGoToWaterGoal")
abstract class CFA_Turtle$TurtleGoToWaterGoal extends MoveToBlockGoal {
    @Shadow @Final
    private Turtle turtle;
    
    public CFA_Turtle$TurtleGoToWaterGoal(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange);
    }
    
    @ModifyExpressionValue(method = "canContinueToUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_canContinueToUse(boolean original) {
        return original || !turtle.getLastFluid().isAir();
    }
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_canUse(boolean original) {
        return original || !turtle.canSwimInFluidType(turtle.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "isValidTarget(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_isValidTarget(boolean original, LevelReader level, BlockPos pos) {
        return original || turtle.canSwimInFluidType(level.getFluidState(pos).getFluidType());
    }
}
