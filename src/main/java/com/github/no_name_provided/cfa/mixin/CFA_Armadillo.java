package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Armadillo.class)
abstract class CFA_Armadillo extends Animal {
    
    protected CFA_Armadillo(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "canStayRolledUp()Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/armadillo/Armadillo;isInLiquid()Z"))
    private boolean cfa_canStayRolledUp(boolean original) {
        return original || !getLastFluid().isAir();
    }
}
