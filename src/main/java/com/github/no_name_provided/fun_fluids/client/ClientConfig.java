package com.github.no_name_provided.fun_fluids.client;

import com.github.no_name_provided.fun_fluids.FunFluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = FunFluids.MODID)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue RENDER_THICK_AIR = BUILDER
            .gameRestart()
            .comment("Should thick air be visible? (Set to true for ease of building.)")
            .define("renderThickAir", false);
    private static final ModConfigSpec.BooleanValue SHOW_THICK_AIR_PARTICLES = BUILDER
            .comment("Show thick air particles?")
            .define("showThickAirParticles", true);
    private static final ModConfigSpec.BooleanValue TRANSPARENT_MIST_PARTICLES = BUILDER
            .comment("Should thick air particles be transparent? (will affect rendering behind fluid)")
            .define("translucentMistParticles", true);
    private static final ModConfigSpec.BooleanValue RENDER_THICK_AIR_OVERLAY = BUILDER
            .comment("Show \"fog\" when submerged in thick air?")
            .define("showThickAirParticles", true);
    private static final ModConfigSpec.IntValue THICK_AIR_PARTICLE_SLOWDOWN_FACTOR = BUILDER
            .comment("Frequency of thick air particles? (higher is less frequent)")
            .defineInRange("thickAirParticleSlowDownFactor", 10, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean renderThickAir;
    public static boolean showThickAirParticles;
    public static boolean translucentMistParticles;
    public static boolean renderUnderThickAirOverlay;
    public static int thickAirParticleSlowDownFactor;

    /**
     * This should update the configurable constants every time this config is loaded or reloaded.
     * */
    @SubscribeEvent
    static void onConfigUpdate(final ModConfigEvent event) {
        // A common crash on server stop is caused by trying to check values that have already been unloaded.
        // #BlameTheNeoForgeTeam
        if (!(event instanceof ModConfigEvent.Unloading) && event.getConfig().getType() == ModConfig.Type.CLIENT) {
            if (event instanceof ModConfigEvent.Loading) {
                // I don't want to force air blocks to rerender on config reload, so I'm flagging this
                // with #gameRestart() above and conditionally refusing to handle it here.
                // Otherwise, there'd be a weird lag after updating this config value in-game
                renderThickAir = RENDER_THICK_AIR.get();
            }
            showThickAirParticles = SHOW_THICK_AIR_PARTICLES.get();
            translucentMistParticles = TRANSPARENT_MIST_PARTICLES.get();
            renderUnderThickAirOverlay = RENDER_THICK_AIR_OVERLAY.get();
            thickAirParticleSlowDownFactor = THICK_AIR_PARTICLE_SLOWDOWN_FACTOR.get();
        }
    }

}
