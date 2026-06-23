package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.CFA_IEntityExtension;
import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class CFA_Entity implements CFA_IEntityExtension {
    @Shadow
    public abstract Level level();
    
    @Shadow @Final
    public EntityFluidInteraction fluidInteraction;
    
    @Shadow protected abstract void doWaterSplashEffect();
    
    @Shadow public abstract EntityType<?> getType();
    
    @Shadow public abstract boolean isInWater();
    
    @Shadow public abstract boolean isInLava();
    
    @Unique
    private FluidType cfa$typeWeAreIn = NeoForgeMod.EMPTY_TYPE.value();
    
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern") // this name is appropriate for an interface injection
    public FluidType getLastFluid() {
        return cfa$typeWeAreIn;
    }
    
    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern") // this name is appropriate for an interface injection
    public void setLastFluid(FluidType lastFluid) {
        cfa$typeWeAreIn = lastFluid;
    }
    
    /**
     * Suppress vanilla splash logic. This is handled with our injection.
     */
    @Redirect(method = "updateFluidInteraction()Z",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;wasTouchingWater:Z", opcode = Opcodes.GETFIELD))
    private boolean cfa_updateFluidInteraction_suppressVanillaSplash(Entity instance) {
        return ((IFluidTypeExtension) getLastFluid()).shouldSplash(getType());
    }
    
    /**
     * Force vanilla to track our registered TaggedFluidTypes. Also, inject splash particle logic.
     */
    @Inject(method = "updateFluidInteraction()Z",
            at = @At("TAIL"), cancellable = true)
    private void cfa_updateFluidInteraction_injectGeneralizedLogic(CallbackInfoReturnable<Boolean> cir) {
        if (isInWater()) {
            setLastFluid(NeoForgeMod.WATER_TYPE.value());
        } else if (isInLava()) {
            setLastFluid(NeoForgeMod.LAVA_TYPE.value());
        }
        // Will be false if we're in a vanilla fluid.
        // Might as well reduce performance impact by preferring vanilla fluids
        if (!cir.getReturnValue()) {
            // Cast the class this code will be injected into to itself,
            // tricking the IDE into not complaining when we reference it
            Entity entity = (Entity) (Object) this;
            NeoForgeRegistries.FLUID_TYPES.forEach(fluidType -> {
                if (!fluidType.isVanilla()) {
                    boolean isInFluid = fluidInteraction.isInFluid(((IFluidTypeExtension) fluidType).getTag());
                    if (isInFluid) {
                        if (((IFluidTypeExtension) fluidType).shouldSplash(getType()) && !((IFluidTypeExtension) getLastFluid()).shouldSplash(getType()) && fluidType != getLastFluid()) {
                            // Update this first, so we can use it in our splash mixin.
                            // Yes, it is ever-so-slightly inefficient to call it twice
                            setLastFluid(fluidType);
                            doWaterSplashEffect();
                        }
                        setLastFluid(fluidType);
                        fluidInteraction.applyCurrentTo(((IFluidTypeExtension) fluidType).getTag(), entity, entity.getFluidMotionScale(fluidType));
                        cir.setReturnValue(true);
                    }
                }
            });
            if (!cir.getReturnValue()) {
                setLastFluid(NeoForgeMod.EMPTY_TYPE.value());
            }
        }
    }
    
    @Redirect(method = "doWaterSplashEffect()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private void cfa_doWaterSplashEffect(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        // TODO: make more efficient, support reused fluid types
        Fluid fluid = BuiltInRegistries.FLUID.stream().filter(f -> f.getFluidType() == getLastFluid()).findFirst().orElse(Fluids.WATER);
        ((IFluidTypeExtension) getLastFluid()).createSplashParticleOnClient(
                fluid,
                level,
                x,
                y,
                z,
                xd,
                yd,
                zd
        );
    }
    
    
    /**
     * Make the game emit appropriate swimming sounds and the swim game event if the player is in <i>any</i> fluid that
     * allows swimsprinting.
     *
     * @param original Whether the player is in water.
     * @return True if the player can "swim" in this fluid; otherwise, false.
     */
    @ModifyExpressionValue(method = "applyMovementEmissionAndPlaySound(Lnet/minecraft/world/entity/Entity$MovementEmission;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean cfa_applyMovementEmissionAndPlaySound_fixIsWater(boolean original) {
        Entity entity = (Entity) (Object) this;
        return original || getLastFluid().canSwim(entity);
    }
    
    /**
     * Despite its name, this method is actually used to determine whether a speed debuff should be applied to players.
     * Without this mixin, swimsprinting players will experience a speed debuff almost exactly equal to the speed buff
     * they get for "sprinting" underwater.
     *
     * @param original The return value of the vanilla method call Entity#isInWater.
     * @return The modified return value to be used elsewhere in Entity#isVisuallyCrawling.
     */
    @ModifyExpressionValue(method = "isVisuallyCrawling()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean Fun_Fluids_isVisuallyCrawling_fixFluidCheck(boolean original) {
        //noinspection ConstantValue - this is a verified compiler error (caused by casting this class to Entity?)
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim((Entity) (Object) (this)) &&
                                !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * A player is in a fluid if it intersects their bounding box.
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean Fun_Fluids_updateSwimming_fixIsInWater(boolean original) {
        //noinspection ConstantValue - this is a verified compiler error (caused by casting this class to Entity?)
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim((Entity) (Object) (this)) &&
                                !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * A player is under a fluid if their eye is covered.
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isUnderWater()Z"))
    private boolean Fun_Fluids_updateSwimming_fixIsUnderWater(boolean original) {
        Entity entity = (Entity) (Object) (this);
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type.canSwim(entity) &&
                                !type.isVanilla() &&
                                this.fluidInteraction.isEyeInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * A player may continue swimming if they're in any swimmable fluid block, so we map those to one that'll pass the
     * downstream vanilla tag check. The rest we map to lava (which fails that tag check).
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"))
    private FluidState cfa_updateSwimming_skipBlockStateCheck(FluidState original) {
        Entity entity = (Entity) (Object) this;
        return !original.getFluidType().canSwim(entity) ? Fluids.LAVA.defaultFluidState() : Fluids.WATER.defaultFluidState();
    }
    
    /**
     * Don't spawn sprint particles while we're under a custom fluid.
     */
    @ModifyExpressionValue(method = "canSpawnSprintParticle()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean Fun_Fluids_canSpawnSprintParticle_fixIsInWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    /**
     * Check for other liquids.
     *
     * @param original A check for water and lava, done by vanilla.
     * @return True if in liquid, false otherwise.
     */
    @ModifyReturnValue(method = "isInLiquid()Z",
            at = @At("RETURN"))
    private boolean Fun_Fluids_isInLiquid(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        !type.isVanilla() &&
                                this.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
    
    @Redirect(method = "sendBubbleColumnParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I", ordinal = 0))
    private static int cfa_sendBubbleColumnParticles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed, @Local(name = "pos", argsOnly = true) BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        return ((IFluidTypeExtension) fluidState.getFluidType()).createSplashParticleOnServer(
                fluidState.getType(),
                level,
                x,
                y,
                z,
                count,
                xDist,
                yDist,
                zDist,
                speed
        );
    }
}
