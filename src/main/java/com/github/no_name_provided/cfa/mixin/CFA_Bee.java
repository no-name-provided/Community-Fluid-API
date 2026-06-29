package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.class)
abstract class CFA_Bee extends Animal implements FlyingAnimal, NeutralMob {
    protected CFA_Bee(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    /**
     * Lets bees drown in modded fluids.
     */
    @ModifyExpressionValue(method = "customServerAiStep(Lnet/minecraft/server/level/ServerLevel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/bee/Bee;isInWater()Z"))
    private boolean cfa_isInWater(boolean original) {
        return original || canDrownInFluidType(getLastFluid());
    }
}
