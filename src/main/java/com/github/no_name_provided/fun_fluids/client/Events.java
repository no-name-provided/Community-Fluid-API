package com.github.no_name_provided.fun_fluids.client;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@EventBusSubscriber(modid=MODID)
public class Events {

    /**
     * This is where we tell Minecraft which fluids are translucent.
     * This doesn't make them invisible, it just allows us to see
     * shapes behind them (like glass).
     * <p>
     * This only has an effect if the texture file contains translucent
     * layers (less than 100 opacity) or empty space.
     * </p>
     * */
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // ItemBlockRenderTypes.setRenderLayer is deprecated for regular blocks, and specified in their json files instead.
        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.THICK_AIR_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get(), RenderType.translucent());
    }

    /**
     * This is where we set fluid tint, under fluid overlay, fluid overlay, still and flowing textures and custom
     * rendering logic.
     * */
    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {

        event.registerFluidType(
                new IClientFluidTypeExtensions() {

                    final ResourceLocation COOL_LAVA_STILL = ResourceLocation.withDefaultNamespace("block/lava_still");
                    final ResourceLocation COOL_LAVA_FLOW = ResourceLocation.withDefaultNamespace("block/lava_flow");

                    // Make sure you either use the parameterless methods (a fallback) or the ones
                    // with all parameters. The versions that just take a fluid state are ignored, and,
                    // if those are all you define, you'll get a null pointer error during fluid tesselation.
                    // #BlameTheNeoForgeTeam
                    @Override
                    public ResourceLocation getStillTexture() {
                        return COOL_LAVA_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return COOL_LAVA_FLOW;
                    }

                },
                FluidRegistries.FunFluidTypes.COOL_LAVA
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {

                    final ResourceLocation THICK_AIR_STILL = ResourceLocation.fromNamespaceAndPath(MODID, "block/thick_air");
                    final ResourceLocation THICK_AIR_FLOW = ResourceLocation.fromNamespaceAndPath(MODID, "block/thick_air");
                    final ResourceLocation THICK_AIR_UNDER_FLUID_OVERLAY = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return THICK_AIR_STILL;
                    }

                    @Override
                    public ResourceLocation getStillTexture() {
                        return THICK_AIR_FLOW;
                    }

                    /**
                     * Returns the location of the texture to apply to the camera when it is
                     * within the fluid. If no location is specified, no overlay will be applied.
                     *
                     * <p>This should return a location to the texture and not a reference
                     * (e.g. {@code minecraft:textures/misc/underwater.png} will use the texture
                     * at {@code assets/minecraft/textures/misc/underwater.png}).
                     *
                     * @param mc the client instance
                     * @return the location of the texture to apply to the camera when it is
                     * within the fluid
                     */
                    @Override
                    public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                        return ClientConfig.renderUnderThickAirOverlay ? THICK_AIR_UNDER_FLUID_OVERLAY : null;
                    }

                    /**
                     * Custom logic run before the fluid renderer. Executed once for each block.
                     * <p>
                     * Return true to disable vanilla rendering (which happens immediately after this code executes).
                     * </p>
                     * */
                    @Override @ParametersAreNonnullByDefault
                    public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
                        // Skip the vanilla fluid renderer?
                        return !ClientConfig.renderThickAir;
                    }
                },
                FluidRegistries.FunFluidTypes.THICK_AIR
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {

                    // THe vanilla water textures are good, grayscale fluid textures. Unless you have an artistic bent,
                    // I'd recommend just using those and applying a tint.
                    final ResourceLocation C_FLUID_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
                    final ResourceLocation C_FLUID_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
                    // This is the only vanilla water texture that's strongly colored, so it probably shouldn't be reused.
                    final ResourceLocation C_FLUID_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");
                    final ResourceLocation UNDER_C_FLUID_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");

                    @Override
                    public ResourceLocation getStillTexture() {
                        return C_FLUID_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return C_FLUID_FLOW;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return C_FLUID_OVERLAY;
                    }

                    @Override
                    public ResourceLocation getRenderOverlayTexture(Minecraft minecraft) {
                        return UNDER_C_FLUID_LOCATION;
                    }

                    /**
                     * Only used as a fallback? Might only apply if biome doesn't have a water color set.
                     *  */
                    @Override
                    public int getTintColor() {
                        // net.neoforged.neoforge.client.ClientNeoForgedMod water_type uses, 0xFF3F76E4
                        // which works out to 255, 63, 118, 228 ARGB, as the default color.
                        // Many other vanilla colors can be found in BiomeColors.

                        // Alpha value will be ignored unless you change the render layer to transparent in the
                        // completely unrelated client setup event.

                        // You can use the FastColor class to easily convert
                        // between decimal components and aggregate ARGB values.
                        // Alternatively, here's an online converter: https://argb-int-calculator.netlify.app/.
                        return ServerConfig.cFColor;
                    }

                    /**Custom logic run before the fluid renderer. Executed once for each block.
                     * Right now, this override is a placeholder created to help me remember the NeoForge team's
                     * unfortunate naming conventions.*/
                    @Override @ParametersAreNonnullByDefault
                    public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
                        // Skip the vanilla fluid renderer?
                        return ServerConfig.cFVisibility;
                    }
                },
                FluidRegistries.FunFluidTypes.C_FLUID
        );
    }
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        // Parameters are the ItemStack and the tint index.
        event.register((stack, tintIndex) -> {
                    if (tintIndex == 1) {
                        return ServerConfig.cFColor;
                    } else {
                        return event.getItemColors().getColor(new ItemStack(Items.BUCKET), 0);
                    }
                },
                ItemRegistry.CONFIGURABLE_FLUID_BUCKET.get()
        );
    }
}
