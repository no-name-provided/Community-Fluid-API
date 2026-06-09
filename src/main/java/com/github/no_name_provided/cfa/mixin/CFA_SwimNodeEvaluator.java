package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SwimNodeEvaluator.class)
abstract class CFA_SwimNodeEvaluator extends NodeEvaluator {
    
    /**
     * Necessary for some fish to move in modded fluids.
     *
     * @param original True if the fluid is water. Otherwise, false.
     * @param pos      The position being checked.
     * @return True if the mob can swim in this fluid or its water, otherwise false.
     */
    @ModifyExpressionValue(method = "getPathTypeOfMob(Lnet/minecraft/world/level/pathfinder/PathfindingContext;IIILnet/minecraft/world/entity/Mob;)Lnet/minecraft/world/level/pathfinder/PathType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_getPathTypeOfMob(boolean original, @Local(name = "pos") BlockPos.MutableBlockPos pos) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() && mob.level().getFluidState(pos).getFluidType().canSwim(mob)
        );
    }
}
