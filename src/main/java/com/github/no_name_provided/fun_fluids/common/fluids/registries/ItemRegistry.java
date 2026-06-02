package com.github.no_name_provided.fun_fluids.common.fluids.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    
    public static final DeferredHolder<Item, BucketItem> COOL_LAVA_BUCKET = ITEMS.register(
            "cool_lava_bucket",
            (identifier) -> new BucketItem(FluidRegistries.FunFluids.COOL_LAVA.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)))
    );
    public static final DeferredHolder<Item, BucketItem> THICK_AIR_BUCKET = ITEMS.register(
            "thick_air_bucket",
            (identifier) -> new BucketItem(FluidRegistries.FunFluids.THICK_AIR_FLUID.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)))
    );
    public static final DeferredHolder<Item, BucketItem> CONFIGURABLE_FLUID_BUCKET = ITEMS.register(
            "configurable_fluid_bucket",
            (identifier) -> new BucketItem(FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)))
    );
    public static final DeferredHolder<Item, BucketItem> RIVER_OF_TIME_BUCKET = ITEMS.register(
            "river_of_time_bucket",
            (identifier) -> new BucketItem(FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, identifier)))
    );
    public static final DeferredHolder<Item, BucketItem> FLOOD_BUCKET = ITEMS.register(
            "flood_bucket",
            (identifier) -> new BucketItem(
                    FluidRegistries.FunFluids.FLOOD_FLUID.get(), new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, identifier))
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET)
                    // This is just me being nice to players and providing a heads-up
                    .component(
                            DataComponents.LORE, ItemLore.EMPTY
                                    .withLineAdded(Component.literal("Extremely Dangerous").withStyle(ChatFormatting.DARK_RED))
                                    .withLineAdded(Component.literal("A flood in a bucket."))
                    ).rarity(Rarity.EPIC)
            )
    );
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
