package com.middlewar.core.model.space;

import com.middlewar.core.projections.AstralObjectLight;
import com.middlewar.core.projections.BaseLight;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
public class PlanetScan {

    private long date;
    private AstralObjectLight planet;
    private Map<String, BaseLight> baseScanned;

    public PlanetScan(Planet planet) {
        this.date = System.currentTimeMillis();
        this.planet = new AstralObjectLight(planet);
        this.baseScanned = new HashMap<>();
    }
}
