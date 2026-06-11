package com.github.no_name_provided.cfa.mixin_interfaces;

import com.github.no_name_provided.cfa.common.tags.CFAFluid;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.Nullable;

/**
 * Used to inject a tag getter into all FluidType subclasses. This way, we don't need to special case vanilla fluids
 * every time we iterate through the registry.
 * <p>
 * Unfortunately, we can't add this to development environments because Neo's interface injection silently fails when
 * used on Neo's own classes.
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
     * Can players fish in this fluid? Must be false unless you also return a valid ResourceKey<Lottable> from
     * #getLootTableKey.
     *
     * @return Tru if players can fish in this fluid; otherwise false.
     */
    default boolean canFish() {
        return getFishingLootTableKey() != null;
    }
    
    /**
     * The loot table to use while fishing in this fluid.
     *
     * @return The ResourceKey pointing at the loot table to be used to roll fishing loot.
     */
    default @Nullable ResourceKey<LootTable> getFishingLootTableKey() {
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
}
