package com.github.no_name_provided.cfa.mixin_interfaces;

import com.github.no_name_provided.cfa.client.particles.CFAParticleTypes;
import com.github.no_name_provided.cfa.client.particles.options.FluidParticleOption;
import com.github.no_name_provided.cfa.common.ClientWrappers;
import com.github.no_name_provided.cfa.common.tags.CFAFluid;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * Used to inject a tag getter into all FluidType subclasses. This way, we don't need to special case vanilla fluids
 * every time we iterate through the registry.
 * <p>
 * Unfortunately, we can't add this to development environments because Neo's interface injection silently fails when
 * used on Neo's own classes.
 * </p>
 * <p>
 * Assumes all implementers are (sub)classes of FluidType, and may throw ClassCastExceptions if this assumption is
 * broken.
 * </p>
 */
@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "unused"})
// This is an API; I don't have to use the hooks I provide
// Neo's static, compile time interface injection silently fails for Neo classes (ie, FluidType), but this interface is still injected at runtime by mixins
public interface IFluidTypeExtension {
    
    /**
     * If you're making a modded fluid, this should be overwritten. Defaults to net.minecraft.tags.FluidTags#WATER.
     *
     * @return The fluid tag associated with your fluid type. This is used to check if an entity is touching your fluid.
     */
    default TagKey<Fluid> getTag() {
        FluidType thisType = (FluidType) this;
        
        // We prefer vanilla tags here, for easier cross-loader compat.
        // Neo tags will grab entries in their vanilla counterparts
        if (thisType == NeoForgeMod.LAVA_TYPE.value()) {
            return FluidTags.LAVA;
        } else if (thisType == NeoForgeMod.EMPTY_TYPE.value()) {
            return CFAFluid.EMPTY;
            // This type is only conditionally registered... because the Neo team loves conditionally registered objects
        } else if (NeoForgeMod.MILK_TYPE.isBound() && thisType == NeoForgeMod.MILK_TYPE.value()) {
            return Tags.Fluids.MILK;
        } else {
            // Let devs know they're being criminally sloppy.
            // You can always override with "return FluidTags.WATER" if this is intentional,
            // but you should really configure stuff properly.
            if (thisType != NeoForgeMod.WATER_TYPE.value()) {
                LogUtils.getLogger().debug("WARNING: Missing fluid tag. Defaulting to water. This will override many settings in FluidType with vanilla behavior.");
            }
            return FluidTags.WATER;
        }
    }
    
    /**
     * When your eye is in this fluid, should your mining speed be decreased? In vanilla, this is true only for water.
     *
     * @return True to nerf mining speed, false to leave it alone.
     */
    default boolean reducesMiningSpeed() {
        return this == NeoForgeMod.WATER_TYPE.value();
    }
    
    /**
     * Should aquatic mobs be able to spawn here? Untested.
     *
     * @return True id they can spawn, false otherwise.
     */
    default boolean canSpawnAquaticMobs(EntityType<?> type) {
        return this == NeoForgeMod.WATER_TYPE.value();
    }
    
    /**
     * Can players fish in this fluid? Must be false unless you also return a valid ResourceKey&lt;LootTable&gt; from
     * {@link IFluidTypeExtension#getFishingLootTableKey(Level, BlockPos)}.
     *
     * @return True if players can fish in this fluid; otherwise false.
     */
    default boolean canFish(Level lureLevel, BlockPos lurePos) {
        return getFishingLootTableKey(lureLevel, lurePos) != null;
    }
    
    /**
     * The loot table to use while fishing in this fluid.
     *
     * @return The ResourceKey pointing at the loot table to be used to roll fishing loot.
     */
    default @Nullable ResourceKey<LootTable> getFishingLootTableKey(Level lureLevel, BlockPos lureBlockPos) {
        return this == NeoForgeMod.WATER_TYPE.value() ? BuiltInLootTables.FISHING : null;
    }
    
    /**
     * Whether an entity can stand on fluids of this type. This just hooks into the vanilla methods, which do not
     * implement things the way you probably expect. Disables swimming, and alters navigation.
     * <p>
     * Not recommended for entities other than striders, unless you inject an equivalent of
     * net.minecraft.world.entity.monster.Strider#floatStrider() into their #tick. Not for NeoForgeMod.EMPTY_TYPE.
     * </p>
     *
     * @param stander The entity attempting to stand on this fluid.
     * @return True if the entity can stand on this fluid, false otherwise.
     */
    default boolean entityCanStandOn(Entity stander) {
        // Uncomment for testing
        // return stander.is(EntityType.STRIDER) && this != NeoForgeMod.EMPTY_TYPE.value();
        return this == NeoForgeMod.LAVA_TYPE.value() && stander.is(EntityType.STRIDER);
    }
    
    /**
     * Should the entity (type) make splashes when it enters this fluid?
     *
     * @param splashingType The type of entity making splashes.
     * @return True to make splashes; otherwise false.
     */
    default boolean shouldSplash(EntityType<?> splashingType) {
        return this != NeoForgeMod.LAVA_TYPE.value() && this != NeoForgeMod.EMPTY_TYPE.value();
    }
    
    /**
     * Create the particle we use for splash effects (wolf shake, water entry, etc.) on the client. May be called from
     * common code. Typically no-ops on server threads.
     */
    default void createSplashParticleOnClient(Fluid fluid, Level level, double x, double y, double z, double xAux, double yAux, double zAux) {
        // ServerLevel no ops on this, so that's what we do here
        if (level.isClientSide()) {
            Optional<Integer> tint = ClientWrappers.getFluidTint(fluid);
            if (tint.isPresent()) {
                level.addParticle(
                        ColorParticleOption.create(
                                CFAParticleTypes.TINTED_SPLASH_PARTICLE.get(),
                                tint.get()
                        ), x,
                        y,
                        z,
                        xAux,
                        yAux,
                        zAux
                );
            } else {
                level.addParticle(ParticleTypes.SPLASH, x, y, z, xAux, yAux, zAux);
            }
        }
    }
    
    /**
     * Create the particle we use for splash effects (wolf shake, water entry, etc.) from the server side.
     */
    default int createSplashParticleOnServer(Fluid fluid, ServerLevel level, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
        if (!fluid.getFluidType().isVanilla()) {
            return level.sendParticles(
                    // The server can't look up the tint, so we send the fluid instead
                    new FluidParticleOption(
                            CFAParticleTypes.TINTED_SPLASH_PARTICLE_FROM_FLUID.get(),
                            fluid
                    ), x,
                    y,
                    z,
                    count,
                    xDist,
                    yDist,
                    zDist,
                    speed
            );
        } else {
            return level.sendParticles(ParticleTypes.SPLASH, x, y, z, count, xDist, yDist, zDist, speed);
        }
    }
    
    /**
     * Create the particle we use for wake effects (fish approaching fishing bob) from the server side.
     */
    default int createWakeParticleOnServer(Fluid fluid, ServerLevel level, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
        if (!fluid.getFluidType().isVanilla()) {
            return level.sendParticles(
                    new FluidParticleOption(
                            CFAParticleTypes.TINTED_WAKE_PARTICLE_FROM_FLUID.get(),
                            fluid
                    ), x,
                    y,
                    z,
                    count,
                    xDist,
                    yDist,
                    zDist,
                    speed
            );
        } else {
            return level.sendParticles(ParticleTypes.FISHING, x, y, z, count, xDist, yDist, zDist, speed);
        }
    }
    
    /**
     * Should the fluid make mobs wet?
     *
     * @param toWet The type of entity getting wet.
     * @return True if it should get wet, false otherwise.
     */
    default boolean makesWet(EntityType<?> toWet) {
        return this != NeoForgeMod.LAVA_TYPE.value() && this != NeoForgeMod.EMPTY_TYPE.value() || ((FluidType) this).getIsWaterLike();
    }
    
    /**
     * Should underwater music play when the player is immersed in this fluid?
     *
     * @return True if the music should play, otherwise false.
     */
    default boolean hasUnderWaterMusic() {
        // Uncomment for testing
        return this != NeoForgeMod.EMPTY_TYPE.value() && this != NeoForgeMod.LAVA_TYPE.value();
//        return ((FluidType) this).getIsWaterLike();
    }
    
    /**
     * Should conduits work in this fluid? And on entities in this fluid?
     *
     * @return True if conduits work; otherwise false.
     */
    default boolean conduitCompatible() {
        // Uncomment for testing
//        return this != NeoForgeMod.EMPTY_TYPE.value();
        return ((FluidType) this).getIsWaterLike();
    }
    
    /**
     * Will touching this fluid hurt this living entity?
     *
     * @param toHurt The living entity being hurt.
     * @return True if the entity should be hurt; otherwise false.
     */
    default boolean hurtsEntity(LivingEntity toHurt) {
        return (((FluidType) this).getIsWaterLike() || (((FluidType) this).getTemperature() <= 20)) && toHurt.isSensitiveToWater();
    }
    
    /**
     * Should projectiles be slowed by this fluid?
     *
     * @param projectile The projectile being slowed.
     * @return True if slowed; otherwise False.
     */
    default boolean slowsProjectiles(Projectile projectile) {
        return this != NeoForgeMod.LAVA_TYPE.value() && this != NeoForgeMod.EMPTY_TYPE.value();
    }
    
    /**
     * What speed multiplier should be applied to projectiles moving through this fluid? Does nothing unless
     * #slowsProjectiles returns True.
     *
     * @param projectile The projectile being slowed.
     * @return The amount to scale its speed by.
     */
    default float getInertia(Projectile projectile) {
        if (this != NeoForgeMod.EMPTY_TYPE.value()) {
            if (projectile instanceof AbstractArrow arrow) {
                return arrow.getWaterInertia();
            } else {
                return 0.6f;
            }
        } else {
            return 1f;
        }
    }
    
    /**
     * Should players be able to trigger the riptide enchantment in this fluid?
     *
     * @return True if players can use riptide while touching this fluid. Otherwise, false.
     */
    default boolean triggersRipTide() {
        return this != NeoForgeMod.LAVA_TYPE.value() && this != NeoForgeMod.EMPTY_TYPE.value();
    }
    
    /**
     * Should this fluid prevent burning (applies to mobs that <i>would</i> burn in sunlight).
     *
     * @return True if touching this fluid will prevent mobs from burning in sunlight. False otherwise.
     */
    default boolean preventsBurning(Entity entity) {
        return ((FluidType) this).getIsWaterLike() || entity.canFluidExtinguish((FluidType) this);
    }
    
    /**
     * Boats are covered in FluidType#supportsBoating. This method is for other vehicles. Minecarts are supported
     * out-of-the-box. Other vehicles will need to implement support.
     *
     * @param vehicle The vehicle to affect.
     * @return True if the vehicle should be affected by fluids of this type. Otherwise, false.
     */
    default boolean affectsVehicle(VehicleEntity vehicle) {
        return this != NeoForgeMod.LAVA_TYPE.value() && this != NeoForgeMod.EMPTY_TYPE.value();
    }
}
