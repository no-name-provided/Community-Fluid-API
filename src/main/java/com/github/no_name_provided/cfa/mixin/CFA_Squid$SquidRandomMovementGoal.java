package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.entity.animal.squid.Squid$SquidRandomMovementGoal")
abstract class CFA_Squid$SquidRandomMovementGoal extends Goal {
    @Shadow @Final
    private Squid squid;
    
    /**
     * @author No Name Provided.
     * @reason Wrapping calls to fields from other classes is annoying.
     */
    @Overwrite
    public void tick() {
        int noActionTime = this.squid.getNoActionTime();
        if (noActionTime > 100) {
            this.squid.movementVector = Vec3.ZERO;
        } else if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || NeoForgeRegistries.FLUID_TYPES.stream().noneMatch(type -> type.canSwim(squid) && squid.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())) || !this.squid.hasMovementVector()) {
            float angle = this.squid.getRandom().nextFloat() * (float) (Math.PI * 2);
            this.squid.movementVector = new Vec3(Mth.cos(angle) * 0.2F, -0.1F + this.squid.getRandom().nextFloat() * 0.2F, Mth.sin(angle) * 0.2F);
        }
    }
}
