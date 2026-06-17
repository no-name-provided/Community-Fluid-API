package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EyeOfEnder.class)
abstract class CFA_EyeOfEnder extends Entity implements ItemSupplier {
    
    public CFA_EyeOfEnder(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "spawnParticles(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/EyeOfEnder;isInWater()Z"))
    private boolean cfa_spawnParticles(boolean original) {
        return !getLastFluid().isAir();
    }
}
