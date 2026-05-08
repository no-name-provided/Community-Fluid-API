package com.github.no_name_provided.fun_fluids.common.fluids;

import com.github.no_name_provided.fun_fluids.common.Events;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.logging.LogUtils;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Example implementation of a fluid with source block and flowing variants, cauldron compatibility, buckets,
 * and generally everything lava does except burn.
 * <p>
 * Note that we inherit from FlowingFluid (rather than BaseFlowingFluid, the convenience class) because we need to
 * overwrite methods and BaseFlowingFluid is too clever for its own good (the subclasses it creates don't inherit from
 * our subclass).
 * */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public abstract class CoolLavaFluid extends FlowingFluid {

    // Common boilerplate. Unnecessary if using BaseFlowingFluid, which doesn't support overrides.
    @Override public FluidType getFluidType() {
        return FluidRegistries.FunFluidTypes.COOL_LAVA.get();
    }

    @Override
    public Fluid getFlowing() {
        return FluidRegistries.FunFluids.FLOWING_COOL_LAVA.get();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistries.FunFluids.COOL_LAVA.get();
    }

    @Override
    public Item getBucket() {
        return ItemRegistry.COOL_LAVA_BUCKET.get();
    }

    @Override
    protected int getSlopeFindDistance(LevelReader worldIn) {
        return worldIn.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA) ? 1 : 2;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == FluidRegistries.FunFluids.COOL_LAVA.get() || fluid == FluidRegistries.FunFluids.FLOWING_COOL_LAVA.get();
    }

    @Override
    protected int getDropOff(LevelReader worldIn) {
        return worldIn.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA) ? 1 : 2;
    }

    @Override
    public int getTickDelay(LevelReader worldIn) {
        return worldIn.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA) ? 10 : 30;
    }

    // End of common boilerplate.

    @Override
    protected @Nullable ParticleOptions getDripParticle() {
        return super.getDripParticle();
    }

//    /**
//     * Returns whether the boat can be used on the fluid.
//     *
//     * @param state the state of the fluid
//     * @param boat  the boat trying to be used on the fluid
//     * @return {@code true} if the boat can be used, {@code false} otherwise
//     */
//    @Override
//    public boolean supportsBoating(FluidState state, Boat boat) {
//        return true;
//    }

    /**
     * Returns whether the block can be extinguished by this fluid.
     *
     * @param state  the state of the fluid
     * @param getter the getter which can get the fluid
     * @param pos    the position of the fluid
     * @return {@code true} if the block can be extinguished, {@code false} otherwise
     */
    @Override
    public boolean canExtinguish(FluidState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    /**
     * Basic animation is handled with an extended texture file and a corresponding mcmeta file. This is for
     * special effects (intermittent effects, dynamic effects, audio).
     * */
    @Override
    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender()) {
            if (random.nextInt(100) == 0) {
                double d0 = (double)pos.getX() + random.nextDouble();
                double d1 = (double)pos.getY() + 1.0;
                double d2 = (double)pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(
                        d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false
                );
            }

            if (random.nextInt(200) == 0) {
                level.playLocalSound(
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        SoundEvents.LAVA_AMBIENT,
                        SoundSource.BLOCKS,
                        0.2F + random.nextFloat() * 0.2F,
                        0.9F + random.nextFloat() * 0.15F,
                        false
                );
            }
        }
    }

    @Override
    public int getSpreadDelay(Level level, BlockPos pos, FluidState currentState, FluidState newState) {
        int i = this.getTickDelay(level);
        if (!currentState.isEmpty()
                && !newState.isEmpty()
                && !currentState.getValue(FALLING)
                && !newState.getValue(FALLING)
                && newState.getHeight(level, pos) > currentState.getHeight(level, pos)
                && level.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    /**
     * We have to handle any fluid interaction event with fluids below our block here. The idiomatic way to handle
     * every other interaction event is with a - you guessed it - registry. Specifically,
     * a wierd one at FluidInteractionRegistry.java.
     * @see //Events#onServerAboutToStart
     * */
    @Override
    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState blockState, Direction direction, FluidState fluidState) {
        if (direction == Direction.DOWN) {
            FluidState fluidstate = level.getFluidState(pos);
            if (fluidstate.is(FluidTags.WATER)) {
                if (blockState.getBlock() instanceof LiquidBlock) {
                    level.setBlock(pos, net.neoforged.neoforge.event.EventHooks.fireFluidPlaceBlockEvent(level, pos, pos, Blocks.STONE.defaultBlockState()), 3);
                }

                level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
                return;
            }
        }

        super.spreadTo(level, pos, blockState, direction, fluidState);
    }
    
    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        //noinspection deprecation - used in vanilla/NeoForge
        return state.getHeight(level, pos) >= 0.44444445F && fluid.is(FluidTags.WATER);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
            return Fluids.EMPTY.defaultFluidState().createLegacyBlock();
    }
    
    @Override protected boolean canConvertToSource(ServerLevel level) {
        return level.getGameRules().get(GameRules.LAVA_SOURCE_CONVERSION);
    }

    public static class Flowing extends CoolLavaFluid {
        
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
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

    public static class Source extends CoolLavaFluid {
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }


    protected CoolLavaFluid() {
        super();
    }
}
