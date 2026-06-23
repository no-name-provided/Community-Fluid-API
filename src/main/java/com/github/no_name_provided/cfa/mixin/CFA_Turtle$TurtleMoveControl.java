package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.turtle.Turtle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtleMoveControl")
abstract class CFA_Turtle$TurtleMoveControl extends MoveControl {
    
    @Shadow @Final
    private Turtle turtle;
    
    public CFA_Turtle$TurtleMoveControl(Mob mob) {
        super(mob);
    }
    
    @ModifyExpressionValue(method = "updateSpeed()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_updateSpeed(boolean original) {
        return original || turtle.canSwimInFluidType(turtle.getLastFluid());
    }
}
