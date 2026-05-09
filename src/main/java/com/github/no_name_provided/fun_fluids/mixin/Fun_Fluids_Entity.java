package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.TaggedFluidType;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class Fun_Fluids_Entity {
    @Shadow @Final
    private EntityFluidInteraction fluidInteraction;
    
//    @Unique
//    boolean functionalFluids$wasTouchingCoolLava = false;
//    @Unique
//    boolean functionalFluids$wasTouchingThickAir = false;
//    @Unique
//    boolean functionalFluids$wasTouchingConfigurableFluid = false;
//    @Unique
//    boolean functionalFluids$wasTouchingRiverOfTimeFluid = false;
//    @Unique
//    boolean functionalFluids$wasTouchingFloodFluid = false;
    
    /**
     * Force vanilla to track our registered TaggedFluidTypes.
     */
    @Inject(method = "updateFluidInteraction()Z",
    at = @At("TAIL"), cancellable = true)
    private void Fun_Fluids_updateFluidInteraction(CallbackInfoReturnable<Boolean> cir) {
        // Might as well reduce performance impact by preferring vanilla fluids
        if (!cir.getReturnValue()) {
            // Cast the class this code will be injected into to itself,
            // tricking the IDE into not complaining when we reference it
            Entity entity = (Entity)(Object)this;
//            functionalFluids$wasTouchingCoolLava = fluidInteraction.isInFluid(FFFluidTagsProvider.COOL_LAVA);
//            functionalFluids$wasTouchingThickAir = fluidInteraction.isInFluid(FFFluidTagsProvider.THICK_AIR);
//            functionalFluids$wasTouchingConfigurableFluid = fluidInteraction.isInFluid(FFFluidTagsProvider.CONFIGURABLE_FLUID);
//            functionalFluids$wasTouchingRiverOfTimeFluid = fluidInteraction.isInFluid(FFFluidTagsProvider.RIVER_OF_TIME);
//            functionalFluids$wasTouchingFloodFluid = fluidInteraction.isInFluid(FFFluidTagsProvider.FLOOD_FLUID);
            
//            if (functionalFluids$wasTouchingCoolLava | functionalFluids$wasTouchingThickAir | functionalFluids$wasTouchingConfigurableFluid | functionalFluids$wasTouchingRiverOfTimeFluid | functionalFluids$wasTouchingFloodFluid) {
//
//            }
            FluidRegistries.FunFluidTypes.FLUID_TYPES.getEntries().forEach(entry -> {
                if (entry.get() instanceof TaggedFluidType type) {
                    boolean isInFluid = fluidInteraction.isInFluid(type.getTag());
                    if (isInFluid) {
                        fluidInteraction.applyCurrentTo(type.getTag(), entity, type.motionScale(entity));
                        cir.setReturnValue(true);
                    }
                }
            });
        }
    }
}
