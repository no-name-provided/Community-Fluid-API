package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartFurnace;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecartFurnace.class)
abstract class CFA_MinecartFurnace extends AbstractMinecart {
    protected CFA_MinecartFurnace(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "getMaxSpeed(Lnet/minecraft/server/level/ServerLevel;)D",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/MinecartFurnace;isInWater()Z"))
    private boolean cfa_getMaxSpeed(boolean original) {
        return original || ((IFluidTypeExtension) this.getLastFluid()).affectsVehicle(this);
    }
    
    @ModifyExpressionValue(method = "applyNaturalSlowdown(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/MinecartFurnace;isInWater()Z"))
    private boolean cfa_applyNaturalSlowdown(boolean original) {
        return original || ((IFluidTypeExtension) this.getLastFluid()).affectsVehicle(this);
    }
}
