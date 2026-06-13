package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.RidingEntitySoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RidingEntitySoundInstance.class)
abstract class CFA_RidingEntitySoundInstance extends AbstractTickableSoundInstance {
    @Shadow @Final
    private Entity entity;
    
    protected CFA_RidingEntitySoundInstance(SoundEvent event, SoundSource source, RandomSource random) {
        super(event, source, random);
    }
    
    /**
     * The Entity version of #isUnderWater actually calculates the current value, rather than using the cached one.
     *
     * @param original True if the entity is underwater; otherwise false.
     * @return Whether the entity is under any fluid that makes things wet.
     */
    @ModifyExpressionValue(method = "shouldNotPlayUnderwaterSound()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isUnderWater()Z"))
    private boolean cfa_shouldNotPlayUnderwaterSound(boolean original) {
        return original ||
                (
                        // Vanilla uses cached value for this check
                        ((IFluidTypeExtension) entity.getLastFluid()).makesWet(entity.getType()) &&
                                // and we're going to see if we can be lazy and cached type for this check, too
                                entity.isEyeInFluid(((IFluidTypeExtension) (entity.getLastFluid())).getTag())
                );
    }
}
