package com.middlewar.core.model.commons;

/**
 * @author LEBOC Philippe
 * Coordinates are writed by Galaxy, Solar System, Astral object number in solar system and base.
 *      Minimum value : [00:00:000:0]
 *      Maximum value : [99:99:999:9]
 * (Exemple: 01:65:621:6)
 */
public class Coordinates {

    private int galaxy;
    private int system;
    private int object;
    private int base;

    public Coordinates(int galaxy, int system, int object, int base){
        setGalaxy(galaxy);
        setSystem(system);
        setObject(object);
        setBase(base);
    }

    public int getGalaxy() {
        return galaxy;
    }

    private void setGalaxy(int galaxy) {
        this.galaxy = galaxy;
    }

    public int getSystem() {
        return system;
    }

    private void setSystem(int system) {
        this.system = system;
    }

    public int getObject() {
        return object;
    }

    private void setObject(int object) {
        this.object = object;
    }

    public int getBase() {
        return base;
    }

    private void setBase(int base) {
        this.base = base;
    }

    public String toString(){
        return String.format("%04d", galaxy) + ":" + String.format("%04d", system) + ":" + String.format("%04d", object) + ":" + String.format("%04d", base);
    }
}
