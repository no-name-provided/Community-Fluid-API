package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlyingPathNavigation.class)
abstract class CFA_FlyingPathNavigation extends PathNavigation {
    
    public CFA_FlyingPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }
    
    @ModifyExpressionValue(method = "canUpdatePath()Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInLiquid()Z"))
    private boolean isInLiquid(boolean original) {
        return original || !mob.getLastFluid().isAir();
    }
}
