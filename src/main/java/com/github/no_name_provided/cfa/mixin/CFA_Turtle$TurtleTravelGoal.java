package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.turtle.Turtle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtleTravelGoal")
abstract class CFA_Turtle$TurtleTravelGoal extends Goal {
    @Shadow @Final
    private Turtle turtle;
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_canUse(boolean original) {
        return original || turtle.canSwimInFluidType(turtle.getLastFluid());
    }
}
