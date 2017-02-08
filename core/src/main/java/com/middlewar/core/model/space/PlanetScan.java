package com.middlewar.core.model.space;

import com.middlewar.core.projections.AstralObjectLight;
import com.middlewar.core.projections.BaseLight;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bertrand.
 */
public class PlanetScan {
    private long date;
    private AstralObjectLight planet;
    private Map<String, BaseLight> baseScanned;

    public PlanetScan() {}

    public PlanetScan(Planet planet) {
        this.date = System.currentTimeMillis();
        this.planet = new AstralObjectLight(planet);
        this.baseScanned = new HashMap<>();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public AstralObjectLight getPlanet() {
        return planet;
    }

    public void setPlanet(AstralObjectLight planet) {
        this.planet = planet;
    }

    public Map<String, BaseLight> getBaseScanned() {
        return baseScanned;
    }

    public void setBaseScanned(Map<String, BaseLight> baseScanned) {
        this.baseScanned = baseScanned;
    }
}
