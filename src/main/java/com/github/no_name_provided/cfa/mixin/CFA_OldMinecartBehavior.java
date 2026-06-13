package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OldMinecartBehavior.class)
abstract class CFA_OldMinecartBehavior extends MinecartBehavior {
    
    protected CFA_OldMinecartBehavior(AbstractMinecart minecart) {
        super(minecart);
    }
    
    @ModifyExpressionValue(method = "moveAlongTrack(Lnet/minecraft/server/level/ServerLevel;)V",
            at = @At(value = "INVOKE", target ="Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;isInWater()Z"))
    private boolean cfa_moveAlongTrack(boolean original) {
        return original || ((IFluidTypeExtension)minecart.getLastFluid()).affectsVehicle(minecart);
    }
    
    @ModifyExpressionValue(method = "getMaxSpeed(Lnet/minecraft/server/level/ServerLevel;)D",
            at = @At(value = "INVOKE", target ="Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;isInWater()Z"))
    private boolean cfa_getMaxSpeed(boolean original) {
        return original || ((IFluidTypeExtension)minecart.getLastFluid()).affectsVehicle(minecart);
    }
}
