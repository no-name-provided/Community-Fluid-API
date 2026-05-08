package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FFRecipeProvider extends RecipeProvider {
    private final RecipeOutput recipeOutput;
    
    public FFRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
        this.recipeOutput = recipeOutput;
    }
    
    @Override
    protected void buildRecipes() {
        shapeless(
                RecipeCategory.MISC,
                ItemStackTemplate.fromNonEmptyStack(new ItemStack(ItemRegistry.COOL_LAVA_BUCKET))
        ).requires(Items.SNOW_BLOCK)
                .requires(Items.MAGMA_CREAM)
                .requires(Items.BUCKET)
                .unlockedBy("has_magma_cream", has(Items.MAGMA_CREAM))
                .save(recipeOutput);
        shapeless(
                RecipeCategory.MISC,
                ItemRegistry.THICK_AIR_BUCKET.get()
        ).requires(Items.DRAGON_BREATH)
                .requires(Items.SNOW_BLOCK)
                .requires(Items.BUCKET)
                .requires(Items.CAMPFIRE)
                .unlockedBy("has_dragon_breath", has(Items.DRAGON_BREATH))
                .save(recipeOutput);
        shapeless(
                RecipeCategory.MISC,
                ItemRegistry.CONFIGURABLE_FLUID_BUCKET.get()
        ).requires(Items.ICE)
                .requires(Items.BUCKET)
                .requires(Items.CAMPFIRE)
                .unlockedBy("has_ice", has(Items.ICE))
                .save(recipeOutput);
        shapeless(
                RecipeCategory.MISC,
                ItemRegistry.RIVER_OF_TIME_BUCKET.get()
        ).requires(Items.ENDER_PEARL)
                .requires(Items.BUCKET)
                .requires(Items.CAMPFIRE)
                .unlockedBy("has_pearl", has(Items.ENDER_PEARL))
                .save(recipeOutput);
        
        // No default recipe for buckets of Flood because they're too dangerous
    }
}
