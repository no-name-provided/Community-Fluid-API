package com.github.no_name_provided.fun_fluids.mixin;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.WaypointTransmitter;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class Fun_Fluids_LivingEntity extends Entity implements Attackable, WaypointTransmitter, net.neoforged.neoforge.common.extensions.ILivingEntityExtension {
    @Shadow
    abstract protected double getEffectiveGravity();
    
    public Fun_Fluids_LivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @Inject(method = "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V",
    at = @At("HEAD"), cancellable = true)
    private void restoreCustomFluidMovement(Vec3 input, FluidState fluidState, CallbackInfo ci) {
        boolean isFalling = this.getDeltaMovement().y <= 0.0;
        double oldY = this.getY();
        double baseGravity = this.getEffectiveGravity();
        
        NeoForgeRegistries.FLUID_TYPES.forEach(fluidType -> {
//            if () {
//
//            }
        });
    }
}
