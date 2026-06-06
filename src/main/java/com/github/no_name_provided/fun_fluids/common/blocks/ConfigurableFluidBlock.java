package com.github.no_name_provided.fun_fluids.common.blocks;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class ConfigurableFluidBlock extends LiquidBlock {
    public ConfigurableFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }
    
    @Override
    protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
        return super.skipRendering(state, neighborState, direction) || ServerConfig.cFVisibility;
    }
}
