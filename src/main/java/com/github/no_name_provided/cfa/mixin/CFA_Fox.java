package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Fox.class)
abstract class CFA_Fox extends Animal {
    
    protected CFA_Fox(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fox/Fox;isInWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || !getLastFluid().isAir();
    }
}
