package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.TaggedFluidType;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
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
abstract class Fun_Fluids_Entity {
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
            FluidRegistries.FunFluidTypes.FLUID_TYPES.getEntries().forEach(entry -> {
                if (entry.get() instanceof TaggedFluidType type) {
                    boolean isInFluid = fluidInteraction.isInFluid(type.getTag());
                    if (isInFluid) {
                        fluidInteraction.applyCurrentTo(type.getTag(), entity, type.motionScale(entity));
                        cir.setReturnValue(true);
                    }
                }
            });
        }
    }
    
    /**
     * A player is in a fluid if it intersects their bounding box.
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean Fun_Fluids_updateSwimming_fixIsInWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                );
    }
    
    /**
     * A player is under a fluid if their eye is covered.
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isUnderWater()Z"))
    private boolean Fun_Fluids_updateSwimming_fixIsUnderWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isEyeInFluid(tagged.getTag())
                );
    }
    
    /**
     * A player may continue swimming if they're in any non-lava, nonempty fluid block, so we map those to one that'll
     * pass the downstream vanilla tag check.
     */
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"))
    private FluidState Fun_Fluids_updateSwimming_skipBlockStateCheck(FluidState original) {
        return original.isEmpty() || original.is(FluidTags.LAVA) ? original : Fluids.WATER.defaultFluidState();
    }
    
    /**
     * Don't spawn sprint particles while we're under a custom fluid.
     */
    @ModifyExpressionValue(method = "canSpawnSprintParticle()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean Fun_Fluids_canSpawnSprintParticle_fixIsInWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                );
    }
    
    /**
     * Check for other liquids.
     * @param original A check for water and lava, done by vanilla.
     * @return True if in liquid, false otherwise.
     */
    @ModifyReturnValue(method = "isInLiquid()Z",
    at = @At("RETURN"))
    private boolean Fun_Fluids_isInLiquid(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                );
    }
}
