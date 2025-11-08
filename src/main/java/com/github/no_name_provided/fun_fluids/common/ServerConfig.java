package com.github.no_name_provided.fun_fluids.common;

import com.github.no_name_provided.fun_fluids.FunFluids;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// Creates and parses the server config, which is copied from the server to all clients during multiplayer.
// This overwrites any server configuration file they may have.
@EventBusSubscriber(modid = FunFluids.MODID)
public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue CF_VISIBILITY = BUILDER.comment("Should the configurable fluid be invisible?").define("cFVisibility", false);
    private static final ModConfigSpec.BooleanValue CF_BOATING = BUILDER.comment("Should the configurable fluid support boats?").define("cFBoating", true);
    private static final ModConfigSpec.BooleanValue CF_HYDRATE = BUILDER.comment("Should the configurable fluid hydrate things?").define("cFHydration", true);
    private static final ModConfigSpec.BooleanValue CF_INFINITE = BUILDER.comment("Should the configurable fluid be infinite?").define("cFInfinite", true);
    private static final ModConfigSpec.BooleanValue CF_EXTINGUISHES = BUILDER.comment("Should the configurable fluid put out fires?").define("cExtinguish", true);
    private static final ModConfigSpec.BooleanValue CF_EVAPORATE_IN_NETHER = BUILDER.comment("Should the configurable fluid evaporate in the nether?").define("cFEvaporateInNether", false);
    private static final ModConfigSpec.BooleanValue CF_RIDE_UNDER = BUILDER.comment("Should players be able to ride under the configurable fluid?").define("cFRideUnder", true);
    private static final ModConfigSpec.BooleanValue CF_DROWN = BUILDER.comment("Can the configurable fluid drown players?").define("cFDrown", true);
    private static final ModConfigSpec.BooleanValue CF_SWIM = BUILDER.comment("Can you swim in the configurable fluid?").define("cFSwim", true);
    private static final ModConfigSpec.DoubleValue CF_PUSH_STRENGTH = BUILDER.comment("How hard should the configurable fluid push? (values between 0 and 0.1 are normal)").defineInRange("cFPushStrength", 0.05, -100.0, 100.0);
    private static final ModConfigSpec.IntValue CF_COLOR = BUILDER.comment("Configurable fluid color (ARGB)?").defineInRange("cFColor", -12618012, Integer.MIN_VALUE, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CF_DAMAGE_MULTIPLIER = BUILDER.comment("How much should the configurable fluid scale fall damage?").defineInRange("cFDamageMultiplier", 0, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue CF_LIGHT = BUILDER.comment("How much light should the configurable fluid emit?").defineInRange("cFLight", 0, 0, 15);
    private static final ModConfigSpec.IntValue CF_RESPONSIVENESS = BUILDER.comment("How responsive should the configurable fluid be (ticks, higher is slower)?").defineInRange("cFResponsiveness", 5, 0, 15);
    private static final ModConfigSpec.IntValue CF_SLOPE_FIND_DISTANCE = BUILDER.comment("How far should the configurable fluid look for height changes before deciding where to flow?").defineInRange("cFHeightChangeDistance", 3, 1, 8);
    private static final ModConfigSpec.EnumValue<Rarity> CF_RARITY = BUILDER.comment("How rare should the configurable fluid be?").defineEnum("cFRarity", Rarity.EPIC);
    private static final ModConfigSpec.BooleanValue FLOOD_DECAYS = BUILDER.comment("Should flood blocks decay into regular water?").define("flood_decays", true);


    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean cFVisibility;
    public static boolean cFBoating;
    public static boolean cFHydrate;
    public static boolean cFInfinite;
    public static boolean cExtinguish;
    public static boolean cFEvaporateInNether;
    public static boolean cFRideUnder;
    public static boolean cFDrown;
    public static boolean cFSwim;
    public static boolean floodDecays;
    public static double cFPushStrength;
    public static int cFColor;
    public static int cFDamageMultiplier;
    public static int cFLight;
    public static int cFResponsiveness;
    public static int cFSLopeFindDistance;
    public static Rarity cFRarity;

    /**
     * This should update the configurable constants every time this config is loaded or reloaded.
     * */
    @SubscribeEvent
    static void onConfigUpdate(final ModConfigEvent event) {
        // A common crash on server stop is caused by trying to check values that have already been unloaded.
        // #BlameTheNeoForgeTeam
        if (!(event instanceof ModConfigEvent.Unloading) && event.getConfig().getType() == ModConfig.Type.SERVER) {
            cFVisibility = CF_VISIBILITY.get();
            cFBoating = CF_BOATING.get();
            cFHydrate = CF_HYDRATE.get();
            cFInfinite = CF_INFINITE.get();
            cExtinguish = CF_EXTINGUISHES.get();
            cFEvaporateInNether = CF_EVAPORATE_IN_NETHER.get();
            cFRideUnder = CF_RIDE_UNDER.get();
            cFDrown = CF_DROWN.get();
            cFSwim = CF_SWIM.get();
            floodDecays = FLOOD_DECAYS.get();
            cFPushStrength = CF_PUSH_STRENGTH.get();
            cFColor = CF_COLOR.get();
            cFDamageMultiplier = CF_DAMAGE_MULTIPLIER.get();
            cFLight = CF_LIGHT.get();
            cFResponsiveness = CF_RESPONSIVENESS.get();
            cFSLopeFindDistance = CF_SLOPE_FIND_DISTANCE.get();
            cFRarity = CF_RARITY.get();
        }
    }
}
