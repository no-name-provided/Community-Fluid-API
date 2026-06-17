package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(Dolphin.class)
abstract class CFA_Dolphin extends AgeableWaterCreature {
    // Sadly, this is static, so we don't actually have a dolphin entity to use. TODO: add an entity-type based method as fallback
    @Shadow
    public static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay() && e.isAlive() && e.getLastFluid().canSwim(null);
    
    protected CFA_Dolphin(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/dolphin/Dolphin;isInWaterOrRain()Z"))
    private boolean cfa_tick_fixMoistness(boolean original) {
        return original || getLastFluid().canHydrate(this) || ((IFluidTypeExtension) getLastFluid()).makesWet(getType());
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/dolphin/Dolphin;isInWater()Z"))
    private boolean cfa_tick_fixIsInWater(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/dolphin/Dolphin;isInWater()Z"))
    private boolean cfa_getAmbientSound(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
}
