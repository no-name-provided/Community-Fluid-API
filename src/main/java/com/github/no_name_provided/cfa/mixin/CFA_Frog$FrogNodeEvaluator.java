package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.frog.Frog$FrogNodeEvaluator")
public class CFA_Frog$FrogNodeEvaluator extends AmphibiousNodeEvaluator {
    
    public CFA_Frog$FrogNodeEvaluator(boolean prefersShallowSwimming) {
        super(prefersShallowSwimming);
    }
    
    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
    private boolean cfa_getStart(boolean original) {
        return original || mob.isPushedByFluid(mob.getLastFluid());
    }
}
