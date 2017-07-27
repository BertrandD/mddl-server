package com.middlewar.core.model.commons;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LEBOC Philippe
 * Coordinates are writed by Galaxy, Solar System, Astral object number in solar system.
 * Minimum value : [00:000:00]
 * Maximum value : [99:999:99]
 * (Exemple: 01:65:621:6)
 */
@Data
@NoArgsConstructor
@Entity
public class Coordinates {

    @Id
    private String id;
    private int galaxy;
    private int system;
    private int object;

    public Coordinates(int galaxy, int system, int object, int base) {
        setId(galaxy + ":" + system + ":" + object);
        setGalaxy(galaxy);
        setSystem(system);
        setObject(object);
    }
}
