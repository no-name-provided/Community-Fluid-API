package com.github.no_name_provided.cfa.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.TryFindLandNearWater;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.mutable.MutableLong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TryFindLandNearWater.class)
public class CFA_TryFindLandNearWater {
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
                                    // Change 1 ----------------------------
                                    if (level.getFluidState(body.blockPosition()).getFluidType().canSwim(body)) {
                                        return false;
                                    } else if (timestamp < nextOkStartTime.longValue()) {
                                        nextOkStartTime.setValue(timestamp + 40L);
                                        return true;
                                    } else {
                                        CollisionContext context = CollisionContext.of(body);
                                        BlockPos bodyBlockPos = body.blockPosition();
                                        BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos();
                                        
                                        label45:
                                        for (BlockPos pos : BlockPos.withinManhattan(bodyBlockPos, range, range, range)) {
                                            if ((pos.getX() != bodyBlockPos.getX() || pos.getZ() != bodyBlockPos.getZ())
                                                    && level.getBlockState(pos).getCollisionShape(level, pos, context).isEmpty()
                                                    && !level.getBlockState(testPos.setWithOffset(pos, Direction.DOWN)).getCollisionShape(level, pos, context).isEmpty()) {
                                                for (Direction direction : Direction.Plane.HORIZONTAL) {
                                                    testPos.setWithOffset(pos, direction);
                                                    // Change 2 ------------------------------------
                                                    if (level.getBlockState(testPos).isAir() && level.getFluidState(testPos.move(Direction.DOWN)).getFluidType().canSwim(body)) {
                                                        lookTarget.set(new BlockPosTracker(pos));
                                                        walkTarget.set(new WalkTarget(new BlockPosTracker(pos), speedModifier, 0));
                                                        break label45;
                                                    }
                                                }
                                            }
                                        }
                                        
                                        nextOkStartTime.setValue(timestamp + 40L);
                                        return true;
                                    }
                                }
                        )
        );
    }
}
