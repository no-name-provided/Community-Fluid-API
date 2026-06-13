package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrowableProjectile.class)
abstract class CFA_ThrowableProjectile extends Projectile {
    
    protected CFA_ThrowableProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "applyInertia()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;isInWater()Z"))
    private boolean cfa_applyInertia(boolean original) {
        return original || isPushedByFluid(getLastFluid());
    }
}
