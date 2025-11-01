package com.github.no_name_provided.fun_fluids.common.fluids.registries;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.blocks.CoolLavaCauldronBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

public class BlockRegistry {
    // We use a dedicated register to avoid a bug in datagen. #Blame the NeoForged team.
    public static final DeferredRegister.Blocks FLUID_BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredHolder<Block, LiquidBlock> COOL_LAVA_BLOCK = FLUID_BLOCKS.register(
            "cool_lava_block",
            () -> new LiquidBlock(
                    FluidRegistries.FunFluids.COOL_LAVA.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.FIRE)
                            .replaceable()
                            .noCollission()
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
            () -> new LiquidBlock(
                    FluidRegistries.FunFluids.THICK_AIR_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .replaceable()
                            .noCollission()
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
            () -> new LiquidBlock(
                    FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GLOW_LICHEN)
                            .replaceable()
                            .noCollission()
                            .strength(100.0f)
                            .lightLevel(state -> ServerConfig.cFLight)
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
            () -> new CoolLavaCauldronBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON).lightLevel(p_152690_ -> 15))
    );

    public static void register(IEventBus bus) {
        SOLID_BLOCKS.register(bus);
        FLUID_BLOCKS.register(bus);
    }
}
