package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class ThickAirFluidType extends FluidType {

    public ThickAirFluidType() {
        super(Properties.create()
                .temperature(20)
                .pathType(PathType.BLOCKED)
                .canConvertToSource(false)
                // Don't scale fall damage
                .fallDistanceModifier(1)
                .canDrown(false)
                .canPushEntity(false)
                .density(9999)
                .fallDistanceModifier(0)
                .motionScale(0)
                .rarity(Rarity.UNCOMMON)
                .supportsBoating(true)
                .viscosity(900)
                .lightLevel(0)
        );
    }

    /**
     * Performs how an entity moves when within the fluid. If the method returns {@code true},
     * no regular movement logic will be performed (the player will simply stop moving).
     * Otherwise, the movement logic for water will be run after your code.
     * <p>
     * This is implicitly called in, and optionally disables most of, LivingEntity#travel.
     * I recommend referencing this method when writing your own logic.
     * </p>
     *
     * @param state        the state of the fluid
     * @param entity       the entity moving within the fluid
     * @param travelVector the velocity of how the entity wants to move
     * @param gravity      the gravity to apply to the entity
     * @return {@code true} if <b>only</b> custom movement logic is performed, {@code false} otherwise
     */
    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double gravity) {
        // The following code is copied, with significant modifications, from LivingEntity#travel.
        // A more robust alternative would be a well-crafted mixin.

        // This bit handles the walking around logic.
        boolean flag = entity.horizontalCollision;
        double entityHeight = entity.getY();
        BlockPos blockpos = entity.getBlockPosBelowThatAffectsMyMovement();
        float f2 = entity.level().getBlockState(entity.getBlockPosBelowThatAffectsMyMovement()).getFriction(entity.level(), entity.getBlockPosBelowThatAffectsMyMovement(), entity);
        float f3 = entity.onGround() ? f2 * 0.91f : 0.91f;
        Vec3 correctedTravelVector = entity.handleRelativeFrictionAndCalculateMovement(travelVector, f2);
        double deltaY = correctedTravelVector.y;
        if (entity.hasEffect(MobEffects.LEVITATION)) {
            //noinspection DataFlowIssue - Shouldn't be null since we just checked for it
            deltaY += (0.05 * (double) (entity.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - correctedTravelVector.y) * 0.2;
        } else //noinspection deprecation - Used in Vanilla/NeoForge
            if (!entity.level().isClientSide || entity.level().hasChunkAt(blockpos)) {
                deltaY -= gravity;
            } else if (entity.getY() > (double) entity.level().getMinBuildHeight()) {
                deltaY = -0.1;
            } else {
                deltaY = 0.0;
            }

        if (entity.onGround()) {
            if (entity.shouldDiscardFriction()) {
                correctedTravelVector = new Vec3(correctedTravelVector.x, deltaY, correctedTravelVector.z);
            } else {
                correctedTravelVector = new Vec3(
                        correctedTravelVector.x * (double) f3,
                        entity instanceof FlyingAnimal ? deltaY * (double) f3 : deltaY * 0.98f,
                        correctedTravelVector.z * (double) f3
                );
            }
            // This bit, which requires an Access Transformer, makes sure things can jump off the ground while immersed
            // With my current setup, this is necessary.
            if (entity.jumping && correctedTravelVector.y() <= 0) {
                correctedTravelVector = new Vec3(
                        correctedTravelVector.x,
                        0.1,
                        correctedTravelVector.z);
            }
        }

        // This bit corrects for floating
        if (!flag) {
            correctedTravelVector = correctedTravelVector.multiply(0.95, 0.8f, 0.95);
            correctedTravelVector = entity.getFluidFallingAdjustedMovement(gravity, flag, correctedTravelVector);
            if (entity.isFree(correctedTravelVector.x, correctedTravelVector.y + 0.6f - entity.getY() + entityHeight, correctedTravelVector.z)) {
                entity.setDeltaMovement(correctedTravelVector.x, 0.3f, correctedTravelVector.z);
            }
        } else {
            correctedTravelVector = correctedTravelVector.multiply(0.5, 0.1f, 0.5);
        }

        // This bit finishes up
        entity.setDeltaMovement(correctedTravelVector);
        return true;
    }
}
