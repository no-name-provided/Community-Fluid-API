package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecartContainer.class)
abstract class CFA_AbstractMinecartContainer extends AbstractMinecart {
    protected CFA_AbstractMinecartContainer(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "applyNaturalSlowdown(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecartContainer;isInWater()Z"))
    private boolean cfa_getMaxSpeed(boolean original) {
        return original || ((IFluidTypeExtension) this.getLastFluid()).affectsVehicle(this);
    }
}
