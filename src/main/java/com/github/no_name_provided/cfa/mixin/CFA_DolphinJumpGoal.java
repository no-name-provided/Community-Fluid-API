package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DolphinJumpGoal.class)
public class CFA_DolphinJumpGoal {
    @Shadow @Final
    private Dolphin dolphin;
    
    @ModifyExpressionValue(method = "waterIsClear(Lnet/minecraft/core/BlockPos;III)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_waterIsClear(boolean original, @Local(name = "nextPos") BlockPos nextPos) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        dolphin.level().getFluidState(nextPos).getFluidType() == type &&
                        type.canSwim(dolphin)
        );
    }
    
    @ModifyExpressionValue(method = "canContinueToUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/dolphin/Dolphin;isInWater()Z"))
    private boolean cfa_canContinueToUse(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        dolphin.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) &&
                        type.canSwim(dolphin)
        );
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        dolphin.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) &&
                        type.canSwim(dolphin)
        );
    }
}
