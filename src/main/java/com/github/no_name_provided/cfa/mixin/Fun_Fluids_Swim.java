package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SuppressWarnings("unused")
@Mixin(Swim.class)
abstract class Fun_Fluids_Swim<T extends Mob> {
    
    /**
     * @author No Name Provided.
     * @reason Using the vanilla result is less efficient. This would allow mobs to swim in modded fluids... if it were
     * actually used. Ignored by many mobs.
     */
    @Overwrite
    public static <T extends Mob> boolean shouldSwim(T mob) {
        return NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type -> {
            TagKey<Fluid> tag = ((IFluidTypeExtension) (type)).getTag();
            // We special case lava here, because vanilla does, too.
            // However, we default to the behavior for water
            return mob.fluidInteraction.isInFluid(tag) && mob.getFluidHeight(tag) > mob.getFluidJumpThreshold() || mob.isInLava();
        });
    }
}
