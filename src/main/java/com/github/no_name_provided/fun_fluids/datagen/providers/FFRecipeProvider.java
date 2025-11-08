package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FFRecipeProvider extends RecipeProvider {

    public FFRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        ShapelessRecipeBuilder.shapeless(
                        RecipeCategory.MISC,
                        new ItemStack(ItemRegistry.COOL_LAVA_BUCKET)
                ).requires(Items.SNOW_BLOCK)
                .requires(Items.LAVA_BUCKET)
                .unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(
                        RecipeCategory.MISC,
                        new ItemStack(ItemRegistry.THICK_AIR_BUCKET)
                ).requires(Items.DRAGON_BREATH)
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_dragon_breath", has(Items.DRAGON_BREATH))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(
                RecipeCategory.MISC,
                new ItemStack(ItemRegistry.CONFIGURABLE_FLUID_BUCKET)
        ).requires(Items.ICE)
                .requires(Items.BUCKET)
                .requires(Items.CAMPFIRE)
                .unlockedBy("has_ice", has(Items.ICE))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(
                        RecipeCategory.MISC,
                        new ItemStack(ItemRegistry.RIVER_OF_TIME_BUCKET)
                ).requires(Items.ENDER_PEARL)
                .requires(Items.BUCKET)
                .requires(Items.CAMPFIRE)
                .unlockedBy("has_pearl", has(Items.ENDER_PEARL))
                .save(recipeOutput);

        // No default recipe for buckets of Flood because they're too dangerous
    }
}
