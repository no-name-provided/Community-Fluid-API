package com.github.no_name_provided.fun_fluids.client;

import com.github.no_name_provided.fun_fluids.client.tints.item.FluidTint;
import com.github.no_name_provided.fun_fluids.common.ServerConfig;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

/**
 * Handles client-only events.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class Events {
    
    /**
     * This is where we set under fluid overlay, fluid overlay, still and flowing textures and custom rendering logic.
     * Other properties (eg, tint) are set in RegisterFluidModelsEvent.
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
                    public @Nullable Identifier getRenderOverlayTexture(Minecraft mc) {
                        return ClientConfig.renderUnderThickAirOverlay ? THICK_AIR_UNDER_FLUID_OVERLAY : null;
                    }
                },
                FluidRegistries.FunFluidTypes.THICK_AIR
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    
                    // The vanilla water textures are good, grayscale fluid textures. Unless you have an artistic bent,
                    // I generally recommend just using those and applying a tint.
                    final Identifier UNDER_C_FLUID_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
                    
                    @Override
                    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                        fluidFogColor.set(ARGB.vector4fFromARGB32(ServerConfig.cFColor));
                    }
                    
                    @Override
                    public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, FogData fog) {
                        float partialTicks = DeltaTracker.ONE.getGameTimeDeltaPartialTick(false);
                        fog.environmentalStart = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_START_DISTANCE, partialTicks);
                        fog.environmentalEnd = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, partialTicks);
                        if (camera.entity() instanceof LocalPlayer player) {
                            fog.environmentalEnd = fog.environmentalEnd * Math.max(0.25F, player.getWaterVision());
                        }
                        
                        fog.skyEnd = fog.environmentalEnd;
                        fog.cloudEnd = fog.environmentalEnd;
                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return UNDER_C_FLUID_LOCATION;
                    }
                },
                FluidRegistries.FunFluidTypes.C_FLUID
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    final Identifier RIVER_OF_TIME_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
                    
                    @Override
                    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                        fluidFogColor.set(new Vector4f(153f / 255, 1, 188f / 255, 1));
                    }
                    
                    @Override
                    public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, FogData fog) {
                        float partialTicks = DeltaTracker.ONE.getGameTimeDeltaPartialTick(false);
                        fog.environmentalStart = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_START_DISTANCE, partialTicks);
                        fog.environmentalEnd = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, partialTicks);
                        if (camera.entity() instanceof LocalPlayer player) {
                            fog.environmentalEnd = fog.environmentalEnd * Math.max(0.25F, player.getWaterVision());
                        }
                        
                        fog.skyEnd = fog.environmentalEnd;
                        fog.cloudEnd = fog.environmentalEnd;
                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return RIVER_OF_TIME_LOCATION;
                    }
                    
                },
                FluidRegistries.FunFluidTypes.RIVER_OF_TIME
        );
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    final Identifier FLOOD_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
                    
                    @Override
                    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
                        fluidFogColor.set(camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_COLOR, camera.getCameraEntityPartialTicks(DeltaTracker.ONE)));
                    }
                    
                    @Override
                    public void modifyFogRender(Camera camera, @Nullable FogEnvironment environment, float renderDistance, float partialTick, FogData fog) {
                        float partialTicks = DeltaTracker.ONE.getGameTimeDeltaPartialTick(false);
                        fog.environmentalStart = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_START_DISTANCE, partialTicks);
                        fog.environmentalEnd = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, partialTicks);
                        if (camera.entity() instanceof LocalPlayer player) {
                            fog.environmentalEnd = fog.environmentalEnd * Math.max(0.25F, player.getWaterVision());
                        }
                        
                        fog.skyEnd = fog.environmentalEnd;
                        fog.cloudEnd = fog.environmentalEnd;
                    }
                    
                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft) {
                        return FLOOD_LOCATION;
                    }
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
                        // Overlay texture is optional (and apparently ignored)
                        null,
                        // So is FluidTintSource. You can leave this out if your texture already has color
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
        // Vanilla water textures are grayscale (until tinted), so are easy for less artistic modders to reuse.
        // This is the only vanilla water texture that's strongly colored, so it probably shouldn't be reused.
        final Identifier C_FLUID_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        // These overlays don't seem to work...
                        new Material(C_FLUID_OVERLAY),
                        _ ->
                                // net.neoforged.neoforge.client.ClientNeoForgedMod water_type uses, 0xFF3F76E4
                                // which works out to 255, 63, 118, 228 ARGB, as the default color.
                                // Many other vanilla colors can be found in BiomeColors.
                                
                                // Alpha value may be ignored unless you fiddle with rendering elsewhere -
                                // testing required.
                                
                                // You can use the ARGB class to easily convert
                                // between decimal components and aggregate ARGB values.
                                // Alternatively, here's an online converter: https://argb-int-calculator.netlify.app/.
                                ServerConfig.cFColor
                ),
                FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_CONFIGURABLE_FLUID.get()
        );
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        new Material(Identifier.withDefaultNamespace("block/water_overlay")),
                        // FluidTintSource is a functional interface, so you can just supply a lambda
                        // that takes a FluidState and returns an integer
                        _ -> -937847206
                ),
                FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_RIVER_OF_TIME_FLUID.get()
        );
        event.register(
                new FluidModel.Unbaked(
                        new Material(Identifier.withDefaultNamespace("block/water_still")),
                        new Material(Identifier.withDefaultNamespace("block/water_flow")),
                        new Material(Identifier.withDefaultNamespace("block/water_overlay")),
                        // If you want your fluid to have conditional tinting, you can provide a full implementation
                        new FluidTintSource() {
                            /**
                             * The default, minimum context, tint. All other methods redirect here, by default.
                             * This is the one method that <i>must</i> be overridden.
                             *
                             * @param state The FluidState being tinted.
                             * @return The color (ARGB, int) to be used.
                             */
                            @Override
                            public int color(FluidState state) {
                                // Taken from net.neoforged.neoforge.client.ClientNeoForgedMod water_type
                                return 0xFF3F76E4;
                            }
                            
                            /**
                             * The tint to apply to blocks in the world. Defaults to color(FluidState).
                             *
                             * @param fluidState The FluidState being tinted.
                             * @param blockState The BlockState being tinted.
                             * @param level The level the fluid is in.
                             * @param pos The BlockPos the fluid occupies.
                             * @return The color (ARGB, int) to be used.
                             */
                            @Override
                            public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                                // This is an example of more advanced, position dependent coloration. We essentially
                                // mimic the color of vanilla water, but make things a bit paler if our fluid is
                                // configured to decay so players can tell the difference.
                                
                                // Determines how much blacker our fluid is. Only applied if it decays to water,
                                // so we get a cool wake effect (and can tell if something goes wrong).
                                int offset = ServerConfig.floodDecays ? 20 : 0;
                                
                                // Get the color of vanilla water
                                int vanillaWaterColor = BiomeColors.getAverageWaterColor(level, pos);
                                // and use the ARGB convenience class to fiddle with
                                // its components (alpha, red, green, blue)
                                int alpha = ARGB.alpha(vanillaWaterColor);
                                int red = Math.max(ARGB.red(vanillaWaterColor) - offset, 0);
                                int green = Math.max(ARGB.green(vanillaWaterColor) - offset, 0);
                                int blue = Math.max(ARGB.blue(vanillaWaterColor) - offset, 0);
                                // Haven't checked, but it's likely that or-ing with the two highest "bits" at max inverts the alpha
                                return ARGB.color(alpha, red, green, blue) | 0xFF000000;
                            }
                            
                            /**
                             * The tint to apply to fluids in inventories (or recipe viewers).
                             * Defaults to color(FluidState).
                             *
                             * @param stack The FluidStack being tinted.
                             * @return The color (ARGB, int) to be used.
                             */
                            @Override
                            public int colorAsStack(FluidStack stack) {
                                return FluidTintSource.super.colorAsStack(stack);
                            }
                            
                            /**
                             * The tint to apply to a fluid when only the blockstate is known.
                             * It's recommended to avoid this, as it (and its default implementation) is incompatible with
                             * <a href="https://github.com/no-name-provided/Functional_Fluids/wiki/Fluidlogging">fluidlogging mods</a>.
                             *
                             * @param state The BlockState being tinted.
                             * @return The color (ARGB, int) to be used.
                             */
                            @Override
                            public int color(BlockState state) {
                                // This is the default implementation, provided only for documentary purposes
                                return this.color(state.getFluidState());
                            }
                            
                            /**
                             * The tint to apply to a fluid in the world when only the blockstate is known.
                             * It's recommended to avoid this, as it (and its default implementation) is incompatible with
                             * <a href="https://github.com/no-name-provided/Functional_Fluids/wiki/Fluidlogging">fluidlogging mods</a>.
                             *
                             * @param state The BlockState being tinted.
                             * @param level A BlockAndTintGetter (probably level) used to retrieve context.
                             * @param pos The BlockPos being tinted.
                             * @return The color (ARGB, int) to be used.
                             */
                            @Override
                            public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
                                // This is the default implementation, provided only for documentary purposes
                                return this.colorInWorld(state.getFluidState(), state, level, pos);
                            }
                        }
                ),
                FluidRegistries.FunFluids.FLOOD_FLUID.get(),
                FluidRegistries.FunFluids.FLOWING_FLOOD_FLUID.get()
        );
    }
    
    @SubscribeEvent
    static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(
                Identifier.fromNamespaceAndPath(MODID, "fluid_contents"),
                FluidTint.CODEC
        );
    }
}
