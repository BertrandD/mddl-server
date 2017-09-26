package com.middlewar.api.manager;

import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.space.Planet;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class PlanetManager {

    public Planet pickRandom() {
        // Pick a random planet from universe
        final Planet jsonPlanet = WorldData.getInstance().getRandomPlanet();

        //return (Planet) astralObjectService.findOneByName(jsonPlanet.getName());
        return jsonPlanet;
    }
}
