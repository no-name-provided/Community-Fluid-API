package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GoalUtils.class)
public class CFA_GoalUtils {
    @ModifyReturnValue(method = "isWater(Lnet/minecraft/world/entity/PathfinderMob;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "RETURN", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean cfa_isWater(boolean original, @Local(name = "mob", argsOnly = true) PathfinderMob mob) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canSwim(mob) &&
                        mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}
