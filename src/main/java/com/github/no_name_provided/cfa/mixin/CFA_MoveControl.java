package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MoveControl.class)
abstract class CFA_MoveControl implements Control {
    
    @Shadow @Final
    protected Mob mob;
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInLiquid()Z"))
    private boolean cfa_tick_fixIsInLiquid(boolean original) {
        return original || mob.canSwimInFluidType(mob.getLastFluid());
    }
    
    // isAffectedByFluids is just true, for nonplayers and for nonflying players... no mixin required
}
