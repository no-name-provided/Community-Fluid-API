package com.github.no_name_provided.cfa.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.TryFindLand;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.mutable.MutableLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TryFindLand.class)
public class CFA_TryFindLand {
    
    /**
     * @author No name provided.
     * @reason Mixins were being finicky. Should be replaced with targeted mixins.
     */
    @Overwrite
    public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
        MutableLong nextOkStartTime = new MutableLong(0L);
        return BehaviorBuilder.create(
                i -> i.group(i.absent(MemoryModuleType.ATTACK_TARGET), i.absent(MemoryModuleType.WALK_TARGET), i.registered(MemoryModuleType.LOOK_TARGET))
                        .apply(
                                i,
                                (attackTarget, walkTarget, lookTarget) -> (level, body, timestamp) -> {
                                    // Change 1 ---------------------------------
                                    if (!level.getFluidState(body.blockPosition()).getFluidType().canSwim(body)) {
                                        return false;
                                    } else if (timestamp < nextOkStartTime.longValue()) {
                                        nextOkStartTime.setValue(timestamp + 60L);
                                        return true;
                                    } else {
                                        BlockPos bodyBlockPos = body.blockPosition();
                                        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
                                        CollisionContext context = CollisionContext.of(body);
                                        
                                        for (BlockPos pos : BlockPos.withinManhattan(bodyBlockPos, range, range, range)) {
                                            if (pos.getX() != bodyBlockPos.getX() || pos.getZ() != bodyBlockPos.getZ()) {
                                                BlockState state = level.getBlockState(pos);
                                                BlockState belowState = level.getBlockState(belowPos.setWithOffset(pos, Direction.DOWN));
                                                // Change 2 ---------------------------
                                                if (!state.getFluidState().getFluidType().canSwim(body)
                                                        && level.getFluidState(pos).isEmpty()
                                                        && state.getCollisionShape(level, pos, context).isEmpty()
                                                        && belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
                                                    BlockPos targetPos = pos.immutable();
                                                    lookTarget.set(new BlockPosTracker(targetPos));
                                                    walkTarget.set(new WalkTarget(new BlockPosTracker(targetPos), speedModifier, 1));
                                                    break;
                                                }
                                            }
                                        }
                                        
                                        nextOkStartTime.setValue(timestamp + 60L);
                                        return true;
                                    }
                                }
                        )
        );
    }
}
