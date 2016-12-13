package com.middlewar.core.interfaces;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {

    default long getMaxVolume() { return 1000; } // todo: remove default !

    default long getVolume() { return 0; } // todo: remove default !

    default long getFreeVolume() { return 1000; } // TODO: FIX ME

    default boolean canBeStored(double amount, long volume) {
        return getFreeVolume() > (amount * volume);
    }

    default double getAmountThatCanBeStored(long volume) {
        if(volume == 0) return (long) Double.POSITIVE_INFINITY;// TODO: FIX ME
        return Math.max(0, Math.floorDiv(getFreeVolume(), volume));
    }
}
