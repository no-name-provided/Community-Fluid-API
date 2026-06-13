package com.github.no_name_provided.cfa.mixin_interfaces;

/**
 * Used to inject a more generic version of the `wasUnderWater`, for efficiency
 * (theoretically, down the line) and so we can react to state changes for things like music selection.
 * <p>
 * These are both overridden in the Entity mixin. The default implementation are just provided to prevent compiler
 * complaints from our static interface injection.
 * </p>
 */
public interface CFA_IPlayerExtension {
    default boolean getWasUnderLastFluid() {
        return false;
    }
    
    default void setWasUnderLastFluid(Boolean wasUnder) {
        //pass
    }
}
