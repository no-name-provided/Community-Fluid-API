package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FloatGoal.class)
abstract class CFA_FloatGoal extends Goal {
    @Shadow @Final
    private Mob mob;
    
    /**
     * @author No Name Provided.
     * @reason Allow mobs to float in modded fluids. Probably more efficient than modifying the return value.
     */
    @Overwrite
    public boolean canUse() {
        return NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type -> {
            TagKey<Fluid> tag = ((IFluidTypeExtension) (type)).getTag();
            // We special case lava here, because vanilla does, too.
            // However, we default to the behavior for water
            return mob.fluidInteraction.isInFluid(tag) && mob.getFluidHeight(tag) > mob.getFluidJumpThreshold() || mob.isInLava();
        });
    }
}
