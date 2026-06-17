package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmphibiousPathNavigation.class)
abstract class CFA_AmphibiousPathNavigation extends PathNavigation {
    
    public CFA_AmphibiousPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }
    
    @ModifyExpressionValue(method = "canMoveDirectly(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInLiquid()Z"))
    private boolean cfa_canMoveDirectly(boolean original) {
        return original || !mob.getLastFluid().isAir();
    }
}
