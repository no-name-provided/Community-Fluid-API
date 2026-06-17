package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.dolphin.Dolphin$DolphinSwimToTreasureGoal")
abstract class CFA_Dolphin$DolphinSwimToTreasureGoal extends Goal {
    @Shadow @Final
    private Dolphin dolphin;
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick(boolean original) {
        return original || dolphin.canSwimInFluidType(dolphin.getLastFluid());
    }
}
