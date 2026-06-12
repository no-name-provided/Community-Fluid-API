package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
abstract class CFA_Mob extends LivingEntity implements Targeting, EquipmentUser, Leashable {
    protected CFA_Mob(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }
    
    /**
     * Allow modded fluids to suppress burning..
     *
     * @param original Whether the mob is in water or rain.
     * @return Whether the mob is in any fluid that can extinguish it or block burning from sunlight.
     */
    @ModifyExpressionValue(method = "isSunBurnTick()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWaterOrRain()Z"))
    private boolean cfa_isSunBurnTick_fixIsInWaterOrRain(boolean original) {
        return original ||
                canFluidExtinguish(getLastFluid()) ||
                ((IFluidTypeExtension) getLastFluid()).blocksBurning(this);
    }
}
