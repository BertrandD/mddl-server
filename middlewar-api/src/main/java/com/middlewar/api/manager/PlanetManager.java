package com.middlewar.api.manager;

import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class PlanetManager {

    @Autowired
    private AstralObjectService astralObjectService;

    public Planet pickRandom() {
        // Pick a random planet from universe
        final Planet jsonPlanet = WorldData.getInstance().getRandomPlanet();

        return (Planet) astralObjectService.findOneByName(jsonPlanet.getName());
    }
}
