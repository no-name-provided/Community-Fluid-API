package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHurtingProjectile.class)
abstract class CFA_AbstractHurtingProjectile extends Projectile {
    
    protected CFA_AbstractHurtingProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "applyInertia()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/hurtingprojectile/AbstractHurtingProjectile;isInWater()Z"))
    private boolean cfa_applyInertia(boolean original) {
        return ((IFluidTypeExtension)getLastFluid()).slowsProjectiles(this);
    }
    
    @ModifyReturnValue(method = "getLiquidInertia()F", at = @At(value = "RETURN"))
    private float cfa_getLiquidInertia(float original) {
        return ((IFluidTypeExtension)getLastFluid()).getInertia(this);
    }
}
