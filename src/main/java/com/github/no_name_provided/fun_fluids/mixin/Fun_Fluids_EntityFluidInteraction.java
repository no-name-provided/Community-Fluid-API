package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.Set;


@Mixin(EntityFluidInteraction.class)
abstract class Fun_Fluids_EntityFluidInteraction {
    
    /**
     * Tell the EntityFluidInteraction tracker to watch modded fluids. This allows EntityFluidInteraction#getFluidHeight
     * to return the correct value.
     * <p>
     * Vanilla fluids are included in the original set.
     * </p>
     */
    @ModifyVariable(method = "<init>(Ljava/util/Set;)V",
            at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/EntityFluidInteraction;<init>(Ljava/util/Set;)V"), argsOnly = true, name = "fluids")
    private static Set<TagKey<Fluid>> Fun_Fluids_entityFluidInteraction(Set<TagKey<Fluid>> fluids) {
        HashSet<TagKey<Fluid>> updatedFluids = HashSet.newHashSet(NeoForgeRegistries.FLUID_TYPES.size());
        NeoForgeRegistries.FLUID_TYPES.forEach(type -> {
            if (!type.isVanilla()) {
                updatedFluids.add(((IFluidTypeExtension) type).getTag());
            }
        });
        updatedFluids.addAll(fluids);
        return updatedFluids;
    }
}
