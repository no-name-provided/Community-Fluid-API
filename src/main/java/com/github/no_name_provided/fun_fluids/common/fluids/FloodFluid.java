package com.github.no_name_provided.fun_fluids.common.fluids;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Demonstrates alternative way to use BseFlowingFluid convenience class. Makes it obvious that overrides on this class
 * do nothing, and allows overrides to be applied to the specific subclasses that will actually be instantiated.
 *
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public abstract class FloodFluid {
    public static BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(
            FluidRegistries.FunFluidTypes.FLOOD,
            FluidRegistries.FunFluids.FLOOD_FLUID,
            FluidRegistries.FunFluids.FLOWING_FLOOD_FLUID
    )
            // We can use lambdas when working with direct values and a supplier is required
            // Ordinarily, this would be our custom bucket. However, we want Flood to function
            // as a pho-water reservoir
            .bucket(() -> Items.WATER_BUCKET)
            .explosionResistance(120.0f)
            .block(BlockRegistry.FLOOD_BLOCK)
            // Half the speed of water
            .tickRate(10);

    /**
     * Normally, this would just be a default instance of $Flowing. However, we specifically want
     * Flood to always produce source blocks. This necessitates a source block with an instance
     * initializer and an override.
     * <p>
     * Incidentally, this is how you use overrides with BaseFlowingFluid. Note that this would be
     * difficult with KaupenJoe's design pattern (demonstrated in RiverOfTimeFluid).
     * </p>
     **/
    public static BaseFlowingFluid FLOWING = new BaseFlowingFluid.Source(PROPERTIES) {
        {
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 8));
        }

        @Override
        public void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    };
    /**
     * If we weren't supporting courtesy config options, there would be no need for overrides here.
     **/
    public static BaseFlowingFluid.Source SOURCE = new BaseFlowingFluid.Source(PROPERTIES) {
        /**
         * Be polite and remove dangerous fluid after it's done spreading.
         * */
        @Override
        protected void spread(Level level, BlockPos pos, FluidState state) {
            if (ServerConfig.destroyFlood) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            } else {
                boolean degrade = !ServerConfig.floodDecays || !this.canSpreadTo(level, pos, level.getBlockState(pos), Direction.DOWN, pos.below(), level.getBlockState(pos.below()), level.getFluidState(pos.below()), this.getNewLiquid(level, pos.below(), level.getBlockState(pos.below())).getType());
                super.spread(level, pos, state);
                if (degrade) {
                    level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
    };

}
