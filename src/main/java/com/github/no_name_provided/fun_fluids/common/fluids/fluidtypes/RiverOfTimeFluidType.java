package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class RiverOfTimeFluidType extends TaggedFluidType {
    public RiverOfTimeFluidType() {
        super(Properties.create()
                // Taken from net.neoforged.neoforge.common.NeoForgeMod.WATER_TYPE
                .fallDistanceModifier(0F)
                .canDrown(false)
                .canExtinguish(true)
                .canConvertToSource(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
        );
    }
    /**
     * Since vanilla has leaned heavily into the use of tags, we're now associating one with each fluid type.
     * This greatly simplifies the mixins required to replace the now-retiring Fluid API.
     */
    @Override
    public TagKey<Fluid> getTag() {
        return FFFluidTagsProvider.RIVER_OF_TIME;
    }
    
    /**
     * Scales the effect of fluid velocity on the velocity of submerged
     * entities (determines how hard the fluid pushes).
     * <p>
     * 0.014d is the default.
     * </p>
     **/
    @Override
    public double motionScale(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isBaby()) {
                // If the entity is a baby, it flows back up the river of time
                return -0.014d;

            } else if (!(entity instanceof Player || entity instanceof AbstractGolem)) {
                // Adults flow down towards the end of the time stream
                return 0.014d;
            }
        }
        // Players, golems and inanimates aren't affected by time
        return 0;
    }
    
    /**
     * Is mining speed reduced while immersed in this fluid?
     */
    @Override
    public boolean reducesMiningSpeed() {
        return true;
    }
}
