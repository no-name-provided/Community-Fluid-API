package com.github.no_name_provided.cfa.mixin;

import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Drowned.class)
abstract class CFA_Drowned extends Zombie implements RangedAttackMob {
    
    public CFA_Drowned(Level level) {
        super(level);
    }
    
    //TODO: This takes a _lot_ of doing, and drowned already wander around okayish in modded fluids, so I'm putting this off for now
}
