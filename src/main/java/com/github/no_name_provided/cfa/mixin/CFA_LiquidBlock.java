package com.github.no_name_provided.cfa.mixin;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@Mixin(LiquidBlock.class)
abstract class CFA_LiquidBlock extends Block {
    @Shadow @Final
    public FlowingFluid fluid;
    
    public CFA_LiquidBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // By default, use the higher of blockstate light and fluid light. If one is unset, it will default to minimum.
        return Math.max(fluid.getFluidType().getLightLevel(), super.getLightEmission(state, level, pos));
    }
}
