package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.CFA_IEntityExtension;
import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.WaypointTransmitter;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class CFA_LivingEntity extends Entity implements Attackable, WaypointTransmitter, net.neoforged.neoforge.common.extensions.ILivingEntityExtension, CFA_IEntityExtension {
    @Shadow
    abstract protected double getEffectiveGravity();
    
    @Shadow
    abstract protected double getDefaultGravity();
    
    @Shadow
    abstract protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY);
    
    @Shadow @Final
    abstract protected void floatInWaterWhileRidden();
    
    @Shadow
    private int noJumpDelay;
    
    @Shadow
    abstract protected void removeFrost();
    
    @Shadow
    abstract protected void tryAddFrost();
    
    @Shadow
    abstract protected void serverAiStep();
    
    @Shadow
    abstract protected void updateFallFlying();
    
    @Shadow
    protected int autoSpinAttackTicks;
    @Shadow
    protected int lerpHeadSteps;
    @Shadow
    protected double lerpYHeadRot;
    @Shadow @Final
    protected EntityEquipment equipment;
    
    @Shadow
    abstract protected void applyInput();
    
    @Shadow
    abstract protected boolean isImmobile();
    
    @Shadow @Final
    protected abstract void travelRidden(Player controller, Vec3 selfInput);
    
    @Shadow
    abstract protected void checkAutoSpinAttack(AABB old, AABB current);
    
    @Shadow
    abstract protected void pushEntities();
    
    @Shadow
    abstract protected void lerpHeadRotationStep(int lerpHeadSteps, double targetYHeadRot);
    
    public CFA_LivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    // Allow our fluids to consistently affect entities
    @ModifyReturnValue(method = "shouldTravelInFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"))
    private boolean Fun_Fluids_shouldTravelInFluid(boolean original, FluidState fluidState) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return original | (!fluidState.isEmpty() && entity.isAffectedByFluids() && !entity.canStandOnFluid(fluidState));
    }
    
    @ModifyExpressionValue(method = "checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;fallDistance:D", opcode = Opcodes.GETFIELD))
    private double cfa_checkFallDamage(double original) {
        return this.getFluidFallDistanceModifier(NeoForgeRegistries.FLUID_TYPES.stream().filter(type ->
                !type.isVanilla() && this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        ).findFirst().orElse(Fluids.EMPTY.getFluidType())) * original;
    }
    
    /**
     *
     * @param input      Movement vector representing the keys pressed by the local player.
     * @param fluidState Vanilla calls always pass in an empty FluidState, which is ignored.
     */
    @Inject(method = "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At("HEAD"), cancellable = true)
    private void Fun_Fluids_travel_in_fluid(Vec3 input, FluidState fluidState, CallbackInfo ci) {
        // Common trick to silence vacuous compiler warnings
        LivingEntity entity = (LivingEntity) (Object) (this);
        
        boolean isFalling = this.getDeltaMovement().y <= 0.0;
        double oldY = this.getY();
        double baseGravity = this.getEffectiveGravity();
        
        // We iterate over the entire FluidType registry, skipping the "vanilla" types
        NeoForgeRegistries.FLUID_TYPES.forEach(fluidType -> {
            if (!fluidType.isVanilla() && fluidType instanceof IFluidTypeExtension taggedFluidType && entity.fluidInteraction.isInFluid(taggedFluidType.getTag())) {
                setLastFluid(fluidType);
                // HANDLE FALL DAMAGE REDUCTION ----------------------------
                // Vanilla handles this in Entity#baseTick (lava) and Entity#move (water). We initially tried to handle
                // it here.However, upstream mixins have consistently failed to have any effect, perhaps because they
                // don't account for impulse. Instead, we target
                // net.minecraft.world.entity.LivingEntity#causeFallDamage... which already has a Neo event injected.
                // So we just use that event and accept a bit of inneficiency.
                // ---------------------------------------------------------
                
                // Conditional has side effects. Returns true if vanilla logic should be skipped.
                if (!entity.moveInFluid(fluidState, input, getEffectiveGravity())) {
                    travelInWater(input, baseGravity, isFalling, oldY);
                    floatInWaterWhileRidden();
                }
                ci.cancel();
            }
        });
    }
    
    /**
     * Some weird choices were made by vanilla here, so we just overwrite the whole thing. This fixes the fluid jumping
     * mechanics... partly, anyway.
     */
    @Inject(method = "aiStep()V", at = @At(value = "HEAD"), cancellable = true)
    private void Fun_Fluids_aiStep(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (noJumpDelay > 0) {
            noJumpDelay--;
        }
        
        if (entity.isInterpolating()) {
            entity.getInterpolation().interpolate();
        } else if (!entity.canSimulateMovement()) {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.98));
        }
        
        if (lerpHeadSteps > 0) {
            lerpHeadRotationStep(lerpHeadSteps, lerpYHeadRot);
            lerpHeadSteps--;
        }
        
        equipment.tick(entity);
        Vec3 movement = entity.getDeltaMovement();
        double dx = movement.x;
        double dy = movement.y;
        double dz = movement.z;
        if (entity.is(EntityType.PLAYER)) {
            if (movement.horizontalDistanceSqr() < 9.0E-6) {
                dx = 0.0;
                dz = 0.0;
            }
        } else {
            if (Math.abs(movement.x) < 0.003) {
                dx = 0.0;
            }
            
            if (Math.abs(movement.z) < 0.003) {
                dz = 0.0;
            }
        }
        
        if (Math.abs(movement.y) < 0.003) {
            dy = 0.0;
        }
        
        entity.setDeltaMovement(dx, dy, dz);
        ProfilerFiller profiler = Profiler.get();
        profiler.push("ai");
        applyInput();
        if (isImmobile()) {
            entity.jumping = false;
            entity.xxa = 0.0F;
            entity.zza = 0.0F;
        } else if (entity.isEffectiveAi() && !entity.level().isClientSide()) {
            profiler.push("newAi");
            serverAiStep();
            profiler.pop();
        }
        
        profiler.pop();
        profiler.push("jump");
        // This is what we need to heavily edit
        if (entity.jumping && entity.isAffectedByFluids()) {
            double fluidHeight;
            if (entity.isInLava()) {
                fluidHeight = entity.getFluidHeight(FluidTags.LAVA);
            } else if (entity.isInWater()) {
                fluidHeight = entity.getFluidHeight(FluidTags.WATER);
            } else {
                fluidHeight = entity.getFluidHeight(
                        ((IFluidTypeExtension) NeoForgeRegistries.FLUID_TYPES.stream().filter(fluidType ->
                                entity.fluidInteraction.isInFluid(((IFluidTypeExtension) fluidType).getTag())
                        ).findFirst().orElse(NeoForgeMod.WATER_TYPE.value())).getTag()
                );
            }
            if (entity.onGround() && fluidHeight < entity.getFluidJumpThreshold()) {
                entity.jumpFromGround();
                noJumpDelay = 10;
            }
            if (fluidHeight > entity.getFluidJumpThreshold()) {
                if (entity.onGround()) {
                    entity.jumpFromGround();
                    noJumpDelay = 10;
                } else if (isInLava()) {
                    entity.jumpInFluid(NeoForgeMod.LAVA_TYPE.value());
                } else if (isInWater()) {
                    entity.jumpInFluid(NeoForgeMod.WATER_TYPE.value());
                } else {
                    var iterator = BuiltInRegistries.FLUID.getTagOrEmpty(((IFluidTypeExtension) getLastFluid()).getTag()).iterator();
                    if (iterator.hasNext()) {
                        entity.jumpInFluid(iterator.next().value().getFluidType());
                    }
                }
            }
        } else {
            noJumpDelay = 0;
        }
        // End heavy editing
        profiler.pop();
        profiler.push("travel");
        if (entity.isFallFlying()) {
            updateFallFlying();
        }
        
        AABB beforeTravelBox = entity.getBoundingBox();
        Vec3 input = new Vec3(entity.xxa, entity.yya, entity.zza);
        if (entity.hasEffect(MobEffects.SLOW_FALLING) || entity.hasEffect(MobEffects.LEVITATION)) {
            resetFallDistance();
        }
        
        if (entity.getControllingPassenger() instanceof Player controller && entity.isAlive()) {
            travelRidden(controller, input);
        } else if (entity.canSimulateMovement() && entity.isEffectiveAi()) {
            entity.travel(input);
        }
        
        if (!entity.level().isClientSide() || entity.isLocalInstanceAuthoritative()) {
            applyEffectsFromBlocks();
        }
        
        if (entity.level().isClientSide()) {
            entity.calculateEntityAnimation(entity instanceof FlyingAnimal);
        }
        
        profiler.pop();
        if (entity.level() instanceof ServerLevel serverLevel) {
            profiler.push("freezing");
            if (!entity.isInPowderSnow || !entity.canFreeze()) {
                entity.setTicksFrozen(Math.max(0, entity.getTicksFrozen() - 2));
            }
            
            removeFrost();
            tryAddFrost();
            if (entity.tickCount % 40 == 0 && entity.isFullyFrozen() && entity.canFreeze()) {
                entity.hurtServer(serverLevel, entity.damageSources().freeze(), 1.0F);
            }
            
            profiler.pop();
        }
        
        profiler.push("push");
        if (autoSpinAttackTicks > 0) {
            autoSpinAttackTicks--;
            checkAutoSpinAttack(beforeTravelBox, entity.getBoundingBox());
        }
        
        pushEntities();
        profiler.pop();
        // More changes here -------------------
        if (entity.level() instanceof ServerLevel serverLevel && ((entity.isSensitiveToWater() && (entity.isInWaterOrRain()) || ((IFluidTypeExtension) entity.getLastFluid()).hurtsEntity(entity)))) {
            entity.hurtServer(serverLevel, entity.damageSources().drown(), 1.0F);
        }
        // -------------------------------------
        ci.cancel();
    }
    
    /**
     * Allow drowning logic to apply to custom fluids. Respect #canDrown.
     */
    @Redirect(method = "baseTick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean Fun_Fluids_baseTick(LivingEntity instance, TagKey<Fluid> tagKey) {
        return instance.isEyeInFluid(tagKey) || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(fluidType ->
                instance.canDrownInFluidType(fluidType) && isEyeInFluid(((IFluidTypeExtension) fluidType).getTag())
        );
    }
    
    @ModifyReturnValue(method = "canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"))
    private boolean cfa_canStandOnFluid(boolean original, @Local(name = "fluid", argsOnly = true) FluidState fluid) {
        return ((IFluidTypeExtension) fluid.getFluidType()).entityCanStandOn(this);
    }
}
