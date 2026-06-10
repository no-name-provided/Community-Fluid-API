package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Only used by Dolphins?
 */
@Mixin(TryFindWaterGoal.class)
abstract class CFA_TryFindWaterGoal extends Goal {
    @Shadow @Final
    private PathfinderMob mob;
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_canUse(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canSwim(mob) &&
                        mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
    
    @ModifyExpressionValue(method = "start()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_start(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canSwim(mob) &&
                        mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}
