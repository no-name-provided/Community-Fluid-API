package com.github.no_name_provided.fun_fluids.common.fluids;

import com.github.no_name_provided.fun_fluids.client.ClientConfig;
import com.github.no_name_provided.fun_fluids.client.registries.ParticleRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example implementation of a fluid that doesn't flow. You'll probably want to use a custom transparent, intangible
 * block for this. That's <i>very</i> easy. However, if you want to code it as a proper fluid, this is one way to do it.
 *
 * <p>We use a flowing fluid here because, in Minecraft, all LiquidBlocks are backed by a flowing fluid.
 * While we could make our own liquid block variant, it's probably more idiomatic to make a flowing fluid
 * and overwrite the methods that result in "flowing" behavior.</p>
 * */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class ThickAirFluid extends FlowingFluid {

    public ThickAirFluid() {
        super();
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 8));
    }

    //Overrides associated with all fluids
    @Override
    public Item getBucket() {
        return ItemRegistry.THICK_AIR_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public Vec3 getFlow(BlockGetter blockReader, BlockPos pos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    public float getHeight(FluidState state, BlockGetter level, BlockPos pos) {
        return getOwnHeight(state);
    }

    @Override
    public float getOwnHeight(FluidState state) {
        // Always a full block
        return 1.0f;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return BlockRegistry.THICK_AIR_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return 8;
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public FluidType getFluidType() {
        return FluidRegistries.FunFluidTypes.THICK_AIR.get();
    }

    /**
     * Basic animation is handled with an extended texture file and a corresponding mcmeta file. This is for
     * special effects (intermittent effects, dynamic effects, audio).
     * */
    @Override
    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (level.isClientSide) { //TODO: make sure this is redundant/this method isn't called on server
            if (ClientConfig.showThickAirParticles && random.nextInt(ClientConfig.thickAirParticleSlowDownFactor) == 0) {
                level.addParticle(
                        ParticleRegistry.MIST_PARTICLE.get(),
                        pos.getX() + nextDouble(-0.5, 1.5),
                        pos.getY() + nextDouble(-.25, 0.5),
                        pos.getZ() + nextDouble(-0.5, 1.5),
                        nextDouble(-.01, .01),
                        nextDouble(-.01, .01),
                        nextDouble(-.01, .01)
                );
            }
        }
    }

    // Overrides associated with flowing fluids
    /**
     * Here we tell the game to just use a source block anytime it would try to generate a flowing block.
     **/
    @Override
    public Fluid getFlowing() {
        return getSource();
    }

    @Override
    public Fluid getSource() {
        return FluidRegistries.FunFluids.THICK_AIR_FLUID.get();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 0;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 8;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 1000;
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    private double nextDouble(double lower, double upper) {
        // We use ThreadLocalRandom here for guaranteed multithreaded performance... and because it has
        // convenience methods for doubles.
        return ThreadLocalRandom.current().nextDouble(lower, upper);
    }
}
