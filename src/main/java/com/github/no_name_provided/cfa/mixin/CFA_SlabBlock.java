package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Does nothing right now, we aren't bothering to get a level reference and query the actual FluidState. Would do
 * nothing even with that unless a fluidlogging mod was installed.
 */
@Mixin(SlabBlock.class)
abstract class CFA_SlabBlock extends Block implements SimpleWaterloggedBlock {
    public CFA_SlabBlock(Properties properties) {
        super(properties);
    }
    
    @ModifyExpressionValue(method = "isPathfindable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/pathfinder/PathComputationType;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_isPathFindable(boolean original, @Local(name = "state", argsOnly = true) BlockState state) {
        // We don't have an entity context here, either...
        return original || state.getFluidState().getFluidType().canSwim(null);
    }
}
