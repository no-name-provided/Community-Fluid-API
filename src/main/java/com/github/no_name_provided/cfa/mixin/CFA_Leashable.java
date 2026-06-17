package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Leashable.class)
public interface CFA_Leashable {
    
    @ModifyExpressionValue(method = "angularFriction(Lnet/minecraft/world/entity/Entity;)F",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInLiquid()Z"))
    private static boolean isInLiquid(boolean original, @Local(name = "entity", argsOnly = true) Entity entity) {
        return original || !entity.getLastFluid().isAir();
    }
}
