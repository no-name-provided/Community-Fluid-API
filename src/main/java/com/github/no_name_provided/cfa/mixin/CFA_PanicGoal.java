package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(PanicGoal.class)
abstract class CFA_PanicGoal extends Goal {
    
    @ModifyArg(method = "lookForWater(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;I)Lnet/minecraft/core/BlockPos;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;findClosestMatch(Lnet/minecraft/core/BlockPos;IILjava/util/function/Predicate;)Ljava/util/Optional;"),
    index = 3)
    private Predicate<BlockPos> lookForWater(Predicate<BlockPos> predicate, @Local(name = "level", argsOnly = true) BlockGetter level) {
        // We may need to check the entity using this, and add additional sanity requirements (canSwim, canDrown, prevents burning, etc.)
        return pos -> !level.getFluidState(pos).getFluidType().isAir();
    }
}
