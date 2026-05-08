package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class ConfigurableFluidType extends FluidType {
    public ConfigurableFluidType() {
        super(Properties.create()
                .canPushEntity(true)
                .canExtinguish(true)
        );
    }

    /**
     * Returns the light level emitted by the fluid.
     *
     * <p>Note: This should be a value between {@code [0,15]}. If not specified, the
     * light level is {@code 0} as most fluids do not emit light.
     *
     * <p>Implementation: This is used by the bucket model to determine whether the fluid
     * should render full-bright when {@code applyFluidLuminosity} is {@code true}.
     *
     * @return the light level emitted by the fluid
     */
    @Override
    public int getLightLevel() {
        return ServerConfig.cFLight;
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
        return ServerConfig.cFPushStrength;
    }

    /**
     * Returns whether the entity can swim in the fluid.
     *
     * @param entity the entity in the fluid
     * @return {@code true} if the entity can swim in the fluid, {@code false} otherwise
     */
    @Override
    public boolean canSwim(Entity entity) {
        return ServerConfig.cFSwim;
    }

    /**
     * Returns how much the fluid should scale the damage done to a falling
     * entity when hitting the ground per tick.
     *
     * <p>Implementation: If the entity is in many fluids, the smallest modifier
     * is applied.
     *
     * @param entity the entity in the fluid
     * @return a scalar to multiply to the fall damage
     */
    @Override
    public float getFallDistanceModifier(Entity entity) {
        return ServerConfig.cFDamageMultiplier;
    }

    /**
     * Returns whether the entity can drown in the fluid.
     *
     * @param entity the entity in the fluid
     * @return {@code true} if the entity can drown in the fluid, {@code false} otherwise
     */
    @Override
    public boolean canDrownIn(LivingEntity entity) {
        return ServerConfig.cFDrown;
    }

    /**
     * Returns whether the boat can be used on the fluid.
     *
     * @param boat the boat trying to be used on the fluid
     * @return {@code true} if the boat can be used, {@code false} otherwise
     */
    @Override public boolean supportsBoating(AbstractBoat boat) {
        return ServerConfig.cFBoating;
    }

    /**
     * Returns whether the entity can ride in this vehicle under the fluid.
     *
     * @param vehicle the vehicle being ridden in
     * @param rider   the entity riding the vehicle
     * @return {@code true} if the vehicle can be ridden in under this fluid,
     * {@code false} otherwise
     */
    @Override
    public boolean canRideVehicleUnder(Entity vehicle, Entity rider) {
        return ServerConfig.cFRideUnder;
    }

    /**
     * Returns whether the block can be extinguished by this fluid.
     *
     * @param state  the state of the fluid
     * @param getter the getter which can get the fluid
     * @param pos    the position of the fluid
     * @return {@code true} if the block can be extinguished, {@code false} otherwise
     */
    @Override
    public boolean canExtinguish(FluidState state, BlockGetter getter, BlockPos pos) {
        return ServerConfig.cExtinguish;
    }

    /**
     * Returns whether the fluid can create a source.
     *
     * @param stack the stack holding the fluid
     * @return {@code true} if the fluid can create a source, {@code false} otherwise
     */
    @Override
    public boolean canConvertToSource(FluidStack stack) {
        return ServerConfig.cFInfinite;
    }

    /**
     * Returns whether the fluid can hydrate.
     *
     * <p>Hydration is an arbitrary word which depends on the implementation.
     *
     * @param stack the stack holding the fluid
     * @return {@code true} if the fluid can hydrate, {@code false} otherwise
     */
    @Override
    public boolean canHydrate(FluidStack stack) {
        return ServerConfig.cFHydrate;
    }

    /**
     * Returns the rarity of the fluid.
     *
     * <p>Note: If not specified, the rarity of the fluid is {@link Rarity#COMMON}.
     *
     * @param stack the stack holding the fluid
     * @return the rarity of the fluid
     */
    @Override
    public Rarity getRarity(FluidStack stack) {
        return ServerConfig.cFRarity;
    }

    /**
     * Determines if this fluid should be vaporized when placed into a level.
     *
     * <p>Note: Fluids that can turn lava into obsidian should vaporize within
     * the nether to preserve the intentions of vanilla.
     *
     * @param level the level the fluid is being placed in
     * @param pos   the position to place the fluid at
     * @param stack the stack holding the fluid being placed
     * @return {@code true} if this fluid should be vaporized on placement, {@code false} otherwise
     * @see //BucketItem#emptyContents(Player, Level, BlockPos, BlockHitResult)
     */
    @SuppressWarnings("deprecation") @Override
    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return ServerConfig.cFEvaporateInNether && level.dimension() == Level.NETHER;
    }

}
