package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.SoundActions;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CoolLavaFluidType extends TaggedFluidType {
    public CoolLavaFluidType() {
        super(Properties.create()
                .fallDistanceModifier(0.0f)
                .canDrown(false)
                .canSwim(false)
                .pathType(PathType.LAVA)
                .adjacentPathType(null)
                .lightLevel(15)
                .density(3000)
                .viscosity(6000)
                // No reason you can't boat on lava. It's dense, and this lava won't burn boats
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canConvertToSource(false)
                // This "lava" is cool!
                .temperature(23)
                .addDripstoneDripping(PointedDripstoneBlock.LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_LAVA, BlockRegistry.COOL_LAVA_CAULDRON.get(), SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON)
        );
    }
    
    /**
     * Since vanilla has leaned heavily into the use of tags, we're now associating one with each fluid type.
     * This greatly simplifies the mixins required to replace the now-retiring Fluid API.
     */
    @Override
    public TagKey<Fluid> getTag() {
        return FFFluidTagsProvider.COOL_LAVA;
    }
    
    /**
     * Returns how much the velocity of the fluid should be scaled by
     * when applied to an entity.
     *
     * @param entity the entity in the fluid
     * @return a scalar to multiply to the fluid velocity
     */
    @Override
    public double motionScale(Entity entity) {
        return entity.level().environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA) ? 0.007D : 0.0023333333333333335D;
    }

    @Override
    public void setItemMovement(ItemEntity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3.x * (double) 0.95F, vec3.y + (double) (vec3.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3.z * (double) 0.95F);
    }

    /**
     * Performs how an entity moves when within the fluid. If using custom
     * movement logic, the method should return {@code true}. Otherwise, the
     * movement logic will default to water.
     * <p>
     *     This is implicitly called in, and optionally disables most of, LivingEntity#travel.
     *     I recommend referencing this method when writing your own logic.
     * </p>
     *
     * @param state          <s>the state of the fluid</s> Fluids.EMPTY#defaultState, unless called by a mod.
     * @param entity         the entity moving within the fluid
     * @param travelVector the velocity of how the entity wants to move
     * @param gravity        the gravity to apply to the entity
     * @return {@code true} if <b>only</b> custom movement logic is performed, {@code false} otherwise
     */
    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double gravity) {
        boolean isFalling = entity.getDeltaMovement().y <= 0.0;
        double oldY = entity.getY();
        // The following code is copied, with minimal changes, from LivingEntity#travelInLava.
        entity.moveRelative(0.02F, travelVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        if (entity.getFluidHeight(FFFluidTagsProvider.COOL_LAVA) <= entity.getFluidJumpThreshold()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.8F, 0.5));
            Vec3 movement = entity.getFluidFallingAdjustedMovement(gravity, isFalling, entity.getDeltaMovement());
            entity.setDeltaMovement(movement);
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
        }
        
        if (gravity != 0.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -gravity / 4.0, 0.0));
        }
        
        entity.jumpOutOfFluid(oldY);
        return true;
    }
}