package com.github.no_name_provided.fun_fluids.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import static com.github.no_name_provided.fun_fluids.CommunityFluidAPI.MODID;

public class CFAFluid {
    // Strangely, there's no vanilla (or Neo) tag for empty fluids. Perhaps to discourage making alternate empty fluids.
    // Recommended you don't use this tag, either for labeling or checking
    public static final TagKey<net.minecraft.world.level.material.Fluid> EMPTY = create("empty");
    
    
    private static TagKey<net.minecraft.world.level.material.Fluid> create(String name) {
        return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(MODID, name));
    }
}
