package com.middlewar.core.model.space;

import com.middlewar.core.holders.AstralObjectHolder;
import com.middlewar.core.holders.BaseHolder;
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
    private AstralObjectHolder planet;
    private Map<String, BaseHolder> baseScanned;

    public PlanetScan(Planet planet) {
        this.date = System.currentTimeMillis();
        this.planet = new AstralObjectHolder(planet);
        this.baseScanned = new HashMap<>();
    }
}
