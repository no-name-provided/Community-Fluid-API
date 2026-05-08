package com.github.no_name_provided.fun_fluids.client;

import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.color.item.FluidContentsTint;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = MODID)
public class Events {
    
    /**
     * This is where we tell Minecraft which fluids are translucent. This doesn't make them invisible, it just allows us
     * to see shapes behind them (like glass).
     * <p>
     * This only has an effect if the texture file contains translucent layers (less than 100 opacity) or empty space.
     * </p>
     *
     */
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // ItemBlockRenderTypes.setRenderLayer is deprecated for regular blocks, and specified in their json files instead.
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.THICK_AIR_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.FLOWING_RIVER_OF_TIME_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.FLOOD_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.FunFluids.FLOWING_FLOOD_FLUID.get(), SingleQuadParticle.Layer.TRANSLUCENT);
    }
    
    /**
     * This is where we set fluid tint, under fluid overlay, fluid overlay, still and flowing textures and custom
     * rendering logic.
     *
     */
    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                },
                FluidRegistries.FunFluidTypes.COOL_LAVA
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    final Identifier THICK_AIR_UNDER_FLUID_OVERLAY = Identifier.withDefaultNamespace("textures/misc/underwater.png");

//                    @Override
//                    public Identifier getFlowingTexture() {
//                        return THICK_AIR_STILL;
//                    }
//
//                    @Override
//                    public Identifier getStillTexture() {
//                        return THICK_AIR_FLOW;
//                    }

//                    /**
//                     * Returns the location of the texture to apply to the camera when it is
//                     * within the fluid. If no location is specified, no overlay will be applied.
//                     *
//                     * <p>This should return a location to the texture and not a reference
//                     * (e.g. {@code minecraft:textures/misc/underwater.png} will use the texture
//                     * at {@code assets/minecraft/textures/misc/underwater.png}).
//                     *
//                     * @param mc the client instance
//                     * @return the location of the texture to apply to the camera when it is
//                     * within the fluid
//                     */
//                    @Override
//                    public @Nullable Identifier getRenderOverlayTexture(Minecraft mc) {
//                        return ClientConfig.renderUnderThickAirOverlay ? THICK_AIR_UNDER_FLUID_OVERLAY : null;
//                    }
//
//                    /**
//                     * Custom logic run before the fluid renderer. Executed once for each block.
//                     * <p>
//                     * Return true to disable vanilla rendering (which happens immediately after this code executes).
//                     * </p>
//                     * */
//                    @Override @ParametersAreNonnullByDefault
//                    public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
//                        // Skip the vanilla fluid renderer?
//                        return !ClientConfig.renderThickAir;
//                    }
                },
                FluidRegistries.FunFluidTypes.THICK_AIR
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    
                    // THe vanilla water textures are good, grayscale fluid textures. Unless you have an artistic bent,
                    // I'd recommend just using those and applying a tint.
                    
                    final Identifier UNDER_C_FLUID_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");

//                    @Override
//                    public Identifier getStillTexture() {
//                        return C_FLUID_STILL;
//                    }
//
//                    @Override
//                    public Identifier getFlowingTexture() {
//                        return C_FLUID_FLOW;
//                    }
//
//                    @Override
//                    public Identifier getOverlayTexture() {
//                        return C_FLUID_OVERLAY;
//                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return UNDER_C_FLUID_LOCATION;
                    }

//                    /**
//                     * Only used as a fallback? Might only apply if biome doesn't have a water color set.
//                     *  */
//                    @Override
//                    public int getTintColor() {
//                        // net.neoforged.neoforge.client.ClientNeoForgedMod water_type uses, 0xFF3F76E4
//                        // which works out to 255, 63, 118, 228 ARGB, as the default color.
//                        // Many other vanilla colors can be found in BiomeColors.
//
//                        // Alpha value will be ignored unless you change the render layer to transparent in the
//                        // unrelated client setup event.
//
//                        // You can use the FastColor class to easily convert
//                        // between decimal components and aggregate ARGB values.
//                        // Alternatively, here's an online converter: https://argb-int-calculator.netlify.app/.
//                        return ServerConfig.cFColor;
//                    }
//
//                    /**
//                     * Custom logic run before the fluid renderer. Executed once for each block.
//                     *
//                     * @param fluidState     the state of the fluid
//                     * @param getter         the getter the fluid can be obtained from
//                     * @param pos            the position of the fluid
//                     * @param vertexConsumer the vertex consumer to emit quads to
//                     * @param blockState     the blockstate at the position of the fluid
//                     * @return               whether vanilla rendering should be skipped
//                     */
//                    @Override @ParametersAreNonnullByDefault
//                    public boolean renderFluid(FluidState fluidState, BlockAndTintGetter getter, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState) {
//                        // Skip the vanilla fluid renderer?
//                        return ServerConfig.cFVisibility;
//                    }
                },
                FluidRegistries.FunFluidTypes.C_FLUID
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    
                    final Identifier RIVER_OF_TIME_STILL = Identifier.withDefaultNamespace("block/water_still");
                    final Identifier RIVER_OF_TIME_FLOW = Identifier.withDefaultNamespace("block/water_flow");
                    final Identifier RIVER_OF_TIME_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
                    final Identifier RIVER_OF_TIME_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");

//                    @Override
//                    public Identifier getStillTexture() {
//                        return RIVER_OF_TIME_STILL;
//                    }
//
//                    @Override
//                    public Identifier getFlowingTexture() {
//                        return RIVER_OF_TIME_FLOW;
//                    }
//
//                    @Override
//                    public Identifier getOverlayTexture() {
//                        return RIVER_OF_TIME_OVERLAY;
//                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return RIVER_OF_TIME_LOCATION;
                    }

//                    /**
//                     * Only used as a fallback? Might only apply if biome doesn't have a water color set.
//                     *  */
//                    @Override
//                    public int getTintColor() {
//                        // You can use the FastColor class to easily convert
//                        // between decimal components and aggregate ARGB values.
//                        // Alternatively, here's an online converter: https://argb-int-calculator.netlify.app/.
//                        return -937847206;
//                    }
                },
                FluidRegistries.FunFluidTypes.RIVER_OF_TIME
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    
                    final Identifier FLOOD_STILL = Identifier.withDefaultNamespace("block/water_still");
                    final Identifier FLOOD_FLOW = Identifier.withDefaultNamespace("block/water_flow");
                    final Identifier FLOOD_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
                    final Identifier FLOOD_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");

//                    @Override
//                    public Identifier getStillTexture() {
//                        return FLOOD_STILL;
//                    }
//
//                    @Override
//                    public Identifier getFlowingTexture() {
//                        return FLOOD_FLOW;
//                    }
//
//                    @Override
//                    public Identifier getOverlayTexture() {
//                        return FLOOD_OVERLAY;
//                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return FLOOD_LOCATION;
                    }

//                    /**
//                     * Fallback. <i>Probably</i> unused.
//                     **/
//                    @Override
//                    public int getTintColor() {
//                        // Taken from net.neoforged.neoforge.client.ClientNeoForgedMod water_type
//                        return 0xFF3F76E4;
//                    }
//
//                    /**
//                     * Example of more advanced, position dependant coloration.
//                     * Also demonstrates usage of the FastColor class to dynamically
//                     * adjust the components of a color.
//                     **/
//                    @Override @ParametersAreNonnullByDefault
//                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
//                        // Determines how much blacker our fluid is. Only applied if it decays to water,
//                        // so we get a cool wake effect (and can tell if something goes wrong).
//                        int offset = ServerConfig.floodDecays ? 20 : 0;
//
//                        int vanillaWaterColor = BiomeColors.getAverageWaterColor(getter, pos);
//                        int alpha = ARGB.alpha(vanillaWaterColor);
//                        int red = Math.max(ARGB.red(vanillaWaterColor) - offset, 0);
//                        int green = Math.max(ARGB.green(vanillaWaterColor) - offset, 0);
//                        int blue = Math.max(ARGB.blue(vanillaWaterColor) - offset, 0);
//                        // Haven't checked, but it's likely that or-ing with the two highest "bits" at max inverts the alpha
//                        return ARGB.color(alpha, red, green, blue) | 0xFF000000;
//                    }
                },
                FluidRegistries.FunFluidTypes.FLOOD
        );
    }
    
    @SubscribeEvent
    static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/lava_still")),
                        new Material(Identifier.withDefaultNamespace("block/lava_flow")),
                        null,
                        null
                ),
                FluidRegistries.FunFluids.COOL_LAVA.get(),
                FluidRegistries.FunFluids.FLOWING_COOL_LAVA.get()
        );
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.fromNamespaceAndPath(MODID, "block/thick_air")),
                        new Material(Identifier.fromNamespaceAndPath(MODID, "block/thick_air")),
                        null,
                        null
                ),
                FluidRegistries.FunFluids.THICK_AIR_FLUID.get()
        );
        // This is the only vanilla water texture that's strongly colored, so it probably shouldn't be reused.
        final Identifier C_FLUID_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        null,
                        _ -> ServerConfig.cFColor
                ),
                FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get()
        );
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        null,
                        _ -> -937847206
                ),
                FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_RIVER_OF_TIME_FLUID.get()
        );
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        null,
                        _ -> 0xFF3F76E4
                ),
                FluidRegistries.FunFluids.FLOOD_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_FLOOD_FLUID.get()
        );
    }
    
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.ItemTintSources event) {
//        event.register(
//                ItemRegistry.CONFIGURABLE_FLUID_BUCKET.getId(),
//                FluidContentsTint.MAP_CODEC
//        );
//        event.register((stack, tintIndex) -> {
//                    if (tintIndex == 1) {
//                        return -937847206;
//                    } else {
//                        return event.getItemColors().getColor(new ItemStack(Items.BUCKET), 0);
//                    }
//                },
//                ItemRegistry.RIVER_OF_TIME_BUCKET.get()
//        );
//        event.register((stack, tintIndex) -> {
//                    if (tintIndex == 1) {
//                        // Make this really distinct.
//                        return -939392767;
//                    } else {
//                        return event.getItemColors().getColor(new ItemStack(Items.BUCKET), 0);
//                    }
//                },
//                ItemRegistry.FLOOD_BUCKET.get()
//        );
    }
}
