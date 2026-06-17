package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlyNodeEvaluator.class)
abstract class CFA_FlyNodeEvaluator extends WalkNodeEvaluator {
    
    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
    private boolean cfa_getStart_fixIsInWater(boolean original) {
        // May want to revisit this and add some conditions, depending on how exactly it's used
        return original || !mob.getLastFluid().isAir();
    }
    
    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_getStart_fixBlockStateCheck(boolean original, @Local(name = "state") BlockState state) {
        // May want to revisit this and add some conditions, depending on how exactly it's used
        return original || (!state.getFluidState().getFluidType().isAir() && state.getBlock() instanceof LiquidBlock);
    }
}
