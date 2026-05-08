package com.github.no_name_provided.fun_fluids.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;
import static com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry.*;
import static com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry.*;

public class FFLanguageProvider extends LanguageProvider {
    public FFLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("item_group." + MODID, "Functional Fluids");

        addFluidSet("Cool Lava", COOL_LAVA_BLOCK, COOL_LAVA_BUCKET, COOL_LAVA_CAULDRON);
        addFluidSet("Thick Air", THICK_AIR_BLOCK, THICK_AIR_BUCKET);
        addFluidSet("Configurable Fluid", CONFIGURABLE_FLUID_BLOCK, CONFIGURABLE_FLUID_BUCKET);
        addFluidSet("River of Time", RIVER_OF_TIME_BLOCK, RIVER_OF_TIME_BUCKET);
        addFluidSet("Flood", FLOOD_BLOCK, FLOOD_BUCKET);

    }

    /**
     * Utility method for common fluid things.
     * */
    private void addFluidSet(String name, Supplier<? extends LiquidBlock> block, Supplier<? extends BucketItem> bucket, Supplier<? extends AbstractCauldronBlock> cauldron) {
        if (null != block) {
            addBlock(block, name + " Block");
            add(block.get().fluid.getFluidType().getDescriptionId(), name);
        }
        if (null != bucket) {
            addItem(bucket, name + " Bucket");
        }
        if (null != cauldron) {
//            addBlock(COOL_LAVA_CAULDRON, name + " Cauldron");
        }
    }

    private void addFluidSet(String name, Supplier<? extends LiquidBlock> block, Supplier<? extends BucketItem> bucket) {
        addFluidSet(name, block, bucket, null);
    }

    private void addFluidSet(String name, Supplier<? extends BucketItem> bucket) {
        addFluidSet(name, null, bucket);
    }

}
