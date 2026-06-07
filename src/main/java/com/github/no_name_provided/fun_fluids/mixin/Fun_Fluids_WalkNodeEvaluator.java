package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WalkNodeEvaluator.class) @MethodsReturnNonnullByDefault
abstract class Fun_Fluids_WalkNodeEvaluator extends NodeEvaluator {
    @Shadow
    protected abstract Node getStartNode(BlockPos pos);
    @Shadow
    protected abstract boolean canStartAt(BlockPos pos);
    
    // The "better" way of updating the method we overwrite below. Unfortunately, some slice magic or somesuch is
    // required for the final piece, and learning that system feels like a poor use of time.
    // Left here for more diligent editors to use
    
//    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
//    private boolean fun_fluids_getStart_fix_isInWater(boolean original) {
//        return NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
//                mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) && type.canSwim(mob)
//        );
//    }
//
//    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z", ordinal = 0))
//    private boolean fun_fluids_getStart_fix_BlockState_check(boolean original, @Local(name = "blockState") BlockState blockState) {
//        return !blockState.liquid();
//    }
    
    /**
     * Force mobs to start pathfinding at the correct y-level, since otherwise they'd spin helplessly.
     *
     * @author No Name Provided.
     * @reason Needed to redirect an inequality, and got tired of looking for documentation.
     */
    @Overwrite
    public Node getStart() {
        BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
        int startY = this.mob.getBlockY();
        BlockState blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
        if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
            if (this.canFloat() && NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                    mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) && type.canSwim(mob)
            )) {
                while (true) {
                    if (!blockState.liquid() && !blockState.getFluidState().isSource()) {
                        startY--;
                        break;
                    }
                    
                    blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), ++startY, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                startY = Mth.floor(this.mob.getY() + 0.5);
            } else {
                reusablePos.set(this.mob.getX(), this.mob.getY() + 1.0, this.mob.getZ());
                
                while (reusablePos.getY() > this.currentContext.level().getMinY()) {
                    startY = reusablePos.getY();
                    reusablePos.setY(reusablePos.getY() - 1);
                    BlockState belowBlockState = this.currentContext.getBlockState(reusablePos);
                    if (!belowBlockState.isAir() && !belowBlockState.isPathfindable(PathComputationType.LAND)) {
                        break;
                    }
                }
            }
        } else {
            while (this.mob.canStandOnFluid(blockState.getFluidState())) {
                blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), ++startY, this.mob.getZ()));
            }
            
            startY--;
        }
        
        BlockPos startPos = this.mob.blockPosition();
        if (!this.canStartAt(reusablePos.set(startPos.getX(), startY, startPos.getZ()))) {
            AABB mobBB = this.mob.getBoundingBox();
            if (this.canStartAt(reusablePos.set(mobBB.minX, startY, mobBB.minZ))
                    || this.canStartAt(reusablePos.set(mobBB.minX, startY, mobBB.maxZ))
                    || this.canStartAt(reusablePos.set(mobBB.maxX, startY, mobBB.minZ))
                    || this.canStartAt(reusablePos.set(mobBB.maxX, startY, mobBB.maxZ))) {
                return this.getStartNode(reusablePos);
            }
        }
        
        return this.getStartNode(new BlockPos(startPos.getX(), startY, startPos.getZ()));
    }
    
    /**
     * Without this mixin, mobs will (mostly) just spin uselessly in modded fluids because they try to path along the
     * bottom of the pool.
     *
     * @param original Whether the current fluid is water.
     * @param pos      The BlockPos being considered.
     * @param level    The level this takes place in.
     * @return Whether the current fluid can be swum in by the mob.
     */
    @ModifyExpressionValue(method = "getFloorLevel(Lnet/minecraft/core/BlockPos;)D",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fun_fluids_getFloorLevel(boolean original, @Local(argsOnly = true, name = "pos") BlockPos pos, @Local(name = "level") BlockGetter level) {
        FluidState state = level.getFluidState(pos);
        return NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                // It's not immediately clear why vanilla treats water different from lava here, so I'm going out on a limb
                // and guessing the difference is the ability to swim in it... though that doesn't seem quite right
                state.is(((IFluidTypeExtension) type).getTag()) && type.canSwim(this.mob)
        );
    }
    
}
