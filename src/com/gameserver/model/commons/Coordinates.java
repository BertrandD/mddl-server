package com.gameserver.model.commons;

/**
 * @author LEBOC Philippe
 * Coordinates are writed by Galaxy, Solar System, Planet number in solar system and base.
 * Every part of coordinates are writed on 4 digits (Exemple: 0020:1565:0001:0016)
 */
public class Coordinates {

    private int galaxy;
    private int system;
    private int planet;
    private int base;

    public Coordinates(int galaxy, int system, int planet, int base){
        setGalaxy(galaxy);
        setSystem(system);
        setPlanet(planet);
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

    public int getPlanet() {
        return planet;
    }

    private void setPlanet(int planet) {
        this.planet = planet;
    }

    public int getBase() {
        return base;
    }

    private void setBase(int base) {
        this.base = base;
    }

    public String toString(){
        return String.format("%04d", galaxy) + ":" + String.format("%04d", system) + ":" + String.format("%04d", planet) + ":" + String.format("%04d", base);
    }
}
