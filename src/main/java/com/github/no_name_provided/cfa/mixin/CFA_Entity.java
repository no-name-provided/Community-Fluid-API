package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class CFA_Entity {
    @Shadow
    public abstract Level level();
    
    @Shadow @Final
    public EntityFluidInteraction fluidInteraction;
    
    /**
     * Force vanilla to track our registered TaggedFluidTypes.
     */
    @Inject(method = "updateFluidInteraction()Z",
            at = @At("TAIL"), cancellable = true)
    private void Fun_Fluids_updateFluidInteraction(CallbackInfoReturnable<Boolean> cir) {
        // Might as well reduce performance impact by preferring vanilla fluids
        if (!cir.getReturnValue()) {
            // Cast the class this code will be injected into to itself,
            // tricking the IDE into not complaining when we reference it
            Entity entity = (Entity) (Object) this;
            NeoForgeRegistries.FLUID_TYPES.forEach(entry -> {
                if (!entry.isVanilla()) {
                    boolean isInFluid = fluidInteraction.isInFluid(((IFluidTypeExtension) entry).getTag());
                    if (isInFluid) {
                        fluidInteraction.applyCurrentTo(((IFluidTypeExtension) entry).getTag(), entity, entry.motionScale(entity));
                        cir.setReturnValue(true);
                    }
                }
            });
        }
    }
    
    /**
     * Despite its name, this  method is actually used to determine whether a speed debuff should be applied to players.
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
    private FluidState Fun_Fluids_updateSwimming_skipBlockStateCheck(FluidState original) {
        Entity entity = (Entity) (Object) this;
        return !original.getType().getFluidType().canSwim(entity) ? Fluids.LAVA.defaultFluidState() : Fluids.WATER.defaultFluidState();
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
}
