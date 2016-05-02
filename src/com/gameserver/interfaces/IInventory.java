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
}
