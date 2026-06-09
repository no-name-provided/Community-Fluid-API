package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.class)
abstract class CFA_BlockBehavior implements FeatureElement {
    
    /**
     * Attempt to fix problems with aquatic mob pathing. Specifically, with random wandering.
     * Appears to have failed.
     */
    @ModifyExpressionValue(method = "isPathfindable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/pathfinder/PathComputationType;)Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_isPathfindable(boolean original, @Local(argsOnly = true, name = "state") BlockState state) {
        // We don't have enough context here, and it would be a nuisance to inject it into every calling method...
        // so we don't
        return original || state.getFluidState().getFluidType().canSwim(null);
    }
}
