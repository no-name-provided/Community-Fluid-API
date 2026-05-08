package com.github.no_name_provided.fun_fluids.common.blocks;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Each type of fluid that can be placed in a cauldron is represented by a separate block. If you want cauldrons to hold
 * your fluids, you'll need to jump through some hoops.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class CoolLavaCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<CoolLavaCauldronBlock> CODEC = simpleCodec(CoolLavaCauldronBlock::new);
    public static final CauldronInteraction.Dispatcher COOL_LAVA_INTERACTION_DISPATCHER = new CauldronInteraction.Dispatcher();
    private static final VoxelShape SHAPE_INSIDE = Block.column(12.0, 4.0, 15.0);
    private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);
    
    @Override
    public MapCodec<CoolLavaCauldronBlock> codec() {
        return CODEC;
    }

    public CoolLavaCauldronBlock(Properties properties) {
        super(properties, COOL_LAVA_INTERACTION_DISPATCHER);
    }
    
    @Override
    protected double getContentHeight(BlockState state) {
        return 0.9375;
    }
    
    @Override
    public boolean isFull(BlockState state) {
        return true;
    }
    
    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return FILLED_SHAPE;
    }
    
    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return 3;
    }
}
