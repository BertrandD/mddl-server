package com.gameserver.interfaces;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {
    long getMaxWeight();
    long getWeight();
    long getFreeWeight();
    long getMaxVolume();
    long getVolume();
    long getFreeVolume();

    default boolean canBeStored(long amount, long volume) {
        return getFreeVolume() > (amount * volume);
    }

    default long getAmountThatCanBeStored(long volume) {
        if(volume == 0) return -1; // TODO: change me
        return Math.max(0, Math.floorDiv(getFreeVolume(), volume));
    }

}
