package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkeletonHorse.class)
abstract class CFA_SkeletonHorse extends AbstractHorse {
    
    protected CFA_SkeletonHorse(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/equine/SkeletonHorse;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick_fixIsLiquid(boolean original) {
        return original || ((IFluidTypeExtension)getLastFluid()).makesWet(EntityType.SKELETON_HORSE);
    }
}
