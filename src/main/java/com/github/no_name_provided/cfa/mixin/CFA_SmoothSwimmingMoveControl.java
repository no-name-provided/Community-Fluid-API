package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SmoothSwimmingMoveControl.class)
abstract class CFA_SmoothSwimmingMoveControl extends MoveControl {
    public CFA_SmoothSwimmingMoveControl(Mob mob) {
        super(mob);
    }
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || mob.canSwimInFluidType(mob.getLastFluid());
    }
}
