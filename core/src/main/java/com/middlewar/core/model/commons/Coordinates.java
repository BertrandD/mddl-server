package com.middlewar.core.model.commons;

import lombok.Data;

/**
 * @author LEBOC Philippe
 * Coordinates are writed by Galaxy, Solar System, Astral object number in solar system.
 *      Minimum value : [00:000:00]
 *      Maximum value : [99:999:99]
 * (Exemple: 01:65:621:6)
 */
@Data
public class Coordinates {

    private int galaxy;
    private int system;
    private int object;

    public Coordinates(int galaxy, int system, int object, int base){
        setGalaxy(galaxy);
        setSystem(system);
        setObject(object);
    }
}
