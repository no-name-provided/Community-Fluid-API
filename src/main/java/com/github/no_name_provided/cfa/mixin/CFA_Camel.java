package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camel.class)
abstract class CFA_Camel extends AbstractHorse {
    
    protected CFA_Camel(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/camel/Camel;isInLiquid()Z"))
    private boolean cfa_tick_fixIsLiquid(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "tick()V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/camel/Camel;isInWater()Z"))
    private boolean cfa_tick_fixIsInWater(boolean original) {
        return original || canDrownInFluidType(getLastFluid());
    }
}
