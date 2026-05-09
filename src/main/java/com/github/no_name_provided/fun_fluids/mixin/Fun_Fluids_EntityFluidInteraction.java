package com.github.no_name_provided.fun_fluids.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider.*;

@Mixin(EntityFluidInteraction.class)
abstract class Fun_Fluids_EntityFluidInteraction {
    
    /**
     * Tell the EntityFluidInteraction tracker to watch our fluids. This allows
     * EntityFluidInteraction#getFluidHeight to return the correct value.
     */
    @ModifyVariable(method = "<init>(Ljava/util/Set;)V",
            at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/EntityFluidInteraction;<init>(Ljava/util/Set;)V"), argsOnly = true, name = "fluids")
    private static Set<TagKey<Fluid>> Fun_Fluids_entityFluidInteraction(Set<TagKey<Fluid>> fluids) {
        HashSet<TagKey<Fluid>> updatedFluids = new HashSet<>(List.of(COOL_LAVA, THICK_AIR, CONFIGURABLE_FLUID, RIVER_OF_TIME, FLOOD_FLUID));
        updatedFluids.addAll(fluids);
        return updatedFluids;
    }
}
