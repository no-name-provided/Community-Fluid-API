package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GoalUtils.class)
public class CFA_GoalUtils {
    @ModifyReturnValue(method = "isWater(Lnet/minecraft/world/entity/PathfinderMob;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "RETURN", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean cfa_isWater(boolean original, @Local(name = "mob", argsOnly = true) PathfinderMob mob, @Local(name = "pos", argsOnly = true) BlockPos pos) {
        return original || mob.canSwimInFluidType(mob.level().getFluidState(pos).getFluidType());
    }
}
