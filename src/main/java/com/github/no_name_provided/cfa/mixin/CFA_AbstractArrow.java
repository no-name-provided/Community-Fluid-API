package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
abstract class CFA_AbstractArrow extends Projectile {
    
    protected CFA_AbstractArrow(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isInWaterOrRain()Z"))
    private boolean cfa_tick_fixIsInWaterOrRain(boolean original) {
        return original || getLastFluid().canExtinguish(this);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isInWater()Z"))
    private boolean cfa_tick_fixIsInWater(boolean original) {
        return original || ((IFluidTypeExtension)getLastFluid()).slowsProjectiles(this);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;getWaterInertia()F"))
    private float cfa_tick_getWaterInertia(float original) {
        return ((IFluidTypeExtension)getLastFluid()).getInertia(this);
    }
}
