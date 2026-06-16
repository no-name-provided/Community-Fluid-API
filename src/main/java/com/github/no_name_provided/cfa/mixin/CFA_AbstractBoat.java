package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractBoat.class)
abstract class CFA_AbstractBoat extends VehicleEntity implements Leashable, net.neoforged.neoforge.common.extensions.IAbstractBoatExtension {
    
    public CFA_AbstractBoat(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @Redirect(method = "onAboveBubbleColumn(ZLnet/minecraft/core/BlockPos;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void cfa_onAboveBubbleColumn(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        ((IFluidTypeExtension)getLastFluid()).createSplashParticleOnClient(
                level.getFluidState(blockPosition()).getType(),
                level,
                x,
                y,
                z,
                xd,
                yd,
                zd
        );
    }
}
