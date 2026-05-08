package com.github.no_name_provided.fun_fluids.common.fluids;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public abstract class ConfigurableFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get();
    }

    @Override public FluidType getFluidType() {
        return FluidRegistries.FunFluidTypes.C_FLUID.get();
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get() || fluid == FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get();
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return ServerConfig.cFSLopeFindDistance;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 2;
    }

    @Override
    public Item getBucket() {
        return ItemRegistry.CONFIGURABLE_FLUID_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return ServerConfig.cFResponsiveness;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return BlockRegistry.CONFIGURABLE_FLUID_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    protected float getExplosionResistance() {
        return 1000;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    public static class Flowing extends ConfigurableFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        
        @Override
        protected boolean canConvertToSource(ServerLevel level) {
            return ServerConfig.cFInfinite;
        }
        
        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends ConfigurableFluid{

        @Override
        protected boolean canConvertToSource(ServerLevel level) {
            return ServerConfig.cFInfinite;
        }
        
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }

}
