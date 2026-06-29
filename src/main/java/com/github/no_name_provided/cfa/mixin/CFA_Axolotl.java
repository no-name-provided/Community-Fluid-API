package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Axolotl.class)
abstract class CFA_AxolotlAxolotl extends Animal implements Bucketable {
    protected CFA_AxolotlAxolotl(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tickBabyAnimations()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_tickBabyAnimations(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "tickAdultAnimations()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_tickAdultAnimations(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "handleAirSupply(Lnet/minecraft/server/level/ServerLevel;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWaterOrRain()Z"))
    private boolean cfa_handleAirSupply(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_hurtServer(boolean original) {
        // This is just for playing dead, not anything to do with breathing
        return original || canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_getAmbientSound(boolean original) {
        return original || canSwimInFluidType(getLastFluid());
    }
    
    /**
     * Force axolotl to respect IEntityExtension#isPushedByFluid.
     */
    @ModifyReturnValue(method = "isPushedByFluid()Z", at = @At(value = "RETURN"))
    private boolean cfa_isPushedByFluid(boolean original) {
        return isPushedByFluid(getLastFluid());
    }
}
