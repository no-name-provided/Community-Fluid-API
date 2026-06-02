package com.github.no_name_provided.fun_fluids.client.tints.item;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FluidTint implements ItemTintSource {
    public static final MapCodec<FluidTint> CODEC = MapCodec.unit(new FluidTint());
    
    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        // Cribbed from the corresponding final neo class with a private constructor,
        // since there isn't really much room for innovation here
        FluidTintSource tintSource = Minecraft.getInstance()
                .getModelManager()
                .getFluidStateModelSet()
                .get(FluidUtil.getFirstStackContained(itemStack).getFluid().defaultFluidState())
                .fluidTintSource();
        return tintSource != null ? tintSource.colorAsStack(FluidUtil.getFirstStackContained(itemStack)) : -1;
    }
    
    @Override
    public MapCodec<FluidTint> type() {
        return CODEC;
    }
}
