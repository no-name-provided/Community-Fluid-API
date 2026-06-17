package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.turtle.Turtle$TurtlePathNavigation")
abstract class CFA_Turtle$TurtlePathNavigation extends AmphibiousPathNavigation {
    
    public CFA_Turtle$TurtlePathNavigation(Mob mob, Level level) {
        super(mob, level);
    }
    
    @ModifyExpressionValue(method = "isStableDestination(Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_isStableDestination(boolean original, @Local(argsOnly = true, name = "pos") BlockPos pos) {
        return original || mob.canSwimInFluidType(mob.level().getFluidState(pos).getFluidType());
    }
}
