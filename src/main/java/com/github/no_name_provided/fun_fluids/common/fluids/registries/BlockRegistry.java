package com.github.no_name_provided.fun_fluids.common.fluids.registries;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.blocks.ConfigurableFluidBlock;
import com.github.no_name_provided.fun_fluids.common.blocks.CoolLavaCauldronBlock;
import com.github.no_name_provided.fun_fluids.common.blocks.ThickAirBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

@SuppressWarnings("unused") // Lambda parameter names provided for clarity - this is a tutorial mod
public class BlockRegistry {
    // We use a dedicated register to avoid a bug in datagen. #BlameTheNeoForgedTeam.
    public static final DeferredRegister.Blocks FLUID_BLOCKS = DeferredRegister.createBlocks(MODID);
    
    public static final DeferredHolder<Block, LiquidBlock> COOL_LAVA_BLOCK = FLUID_BLOCKS.register(
            "cool_lava_block",
            (identifier) -> new LiquidBlock(
                    FluidRegistries.FunFluids.COOL_LAVA.get(),
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, identifier))
                            .mapColor(MapColor.FIRE)
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .lightLevel(state -> 15)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
            )
    );
    public static final DeferredHolder<Block, LiquidBlock> THICK_AIR_BLOCK = FLUID_BLOCKS.register(
            "thick_air_block",
            (identifier) -> new ThickAirBlock(
                    FluidRegistries.FunFluids.THICK_AIR_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, identifier))
                            .mapColor(MapColor.NONE)
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .lightLevel(state -> 0)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
            )
    );
    public static final DeferredHolder<Block, LiquidBlock> CONFIGURABLE_FLUID_BLOCK = FLUID_BLOCKS.register(
            "configurable_fluid_block",
            (identifier) -> new ConfigurableFluidBlock(
                    FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, identifier))
                            .mapColor(MapColor.GLOW_LICHEN)
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .lightLevel(state -> ServerConfig.cFLight)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
            )
    );
    public static final DeferredHolder<Block, LiquidBlock> RIVER_OF_TIME_BLOCK = FLUID_BLOCKS.register(
            "river_of_time_block",
            (identifier) -> new LiquidBlock(
                    FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, identifier))
                            .mapColor(MapColor.COLOR_CYAN)
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .lightLevel(state -> 2)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
            )
    );
    public static final DeferredHolder<Block, LiquidBlock> FLOOD_BLOCK = FLUID_BLOCKS.register(
            "flood_block",
            (identifier) -> new LiquidBlock(
                    FluidRegistries.FunFluids.FLOOD_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, identifier))
                            .mapColor(MapColor.WATER)
                            .replaceable()
                            .noCollision()
                            .strength(100.0f)
                            .lightLevel(state -> 0)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.EMPTY)
            )
    );
    
    public static final DeferredRegister.Blocks SOLID_BLOCKS = DeferredRegister.createBlocks(MODID);


    @SuppressWarnings("deprecation")
    public static final DeferredHolder<Block, AbstractCauldronBlock> COOL_LAVA_CAULDRON = SOLID_BLOCKS.register(
        "cool_lava_cauldron_block",
            (identifier) ->
                    new CoolLavaCauldronBlock(
                            BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON)
                                    .setId(ResourceKey.create(Registries.BLOCK, identifier))
                                    .lightLevel(state -> 15)
                    )
    );
    
    public static void registerSolids(IEventBus bus) {
        SOLID_BLOCKS.register(bus);
    }
    
    public static void registerLiquids(IEventBus bus) {
        FLUID_BLOCKS.register(bus);
    }
}
