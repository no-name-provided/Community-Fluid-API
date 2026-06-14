package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned.DrownedSwimUpGoal")
abstract class CFA_Drowned$DrownedSwimUpGoal extends Goal {
    @Shadow @Final
    private Drowned drowned;
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_canUse(boolean original) {
        return original || drowned.canSwimInFluidType(drowned.getLastFluid());
    }
}
