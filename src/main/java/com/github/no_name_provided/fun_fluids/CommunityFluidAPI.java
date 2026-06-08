package com.github.no_name_provided.fun_fluids;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * This is a from-scratch rewrite of the old, neglected, unjustly hated, and ultimately abandoned NeoForge Fluid API.
 * <p>
 * Contributors welcome.
 * </p>
 * <p>
 * Disclaimer: This mod is the product of my personal experience, made in near isolation. It may have errors or
 * inconsistencies. Feel free to make suggestions. Just remember - word of god doesn't mean anything when it comes to
 * (Neo)Forge. Don't tell me I'm wrong until you've checked for yourself.
 * </p>
 */
@Mod(CommunityFluidAPI.MODID)
public class CommunityFluidAPI {
    
    // Use this (almost) anywhere you need a unique identifier.
    // If two mods have the same ID, they probably won't work together.
    public static final String MODID = "cfa";
    
    // Mod entry point. Mostly used for registration.
    public CommunityFluidAPI(IEventBus modEventBus, ModContainer modContainer) {
    
    }
}
