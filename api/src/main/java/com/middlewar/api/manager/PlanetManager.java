package com.middlewar.api.manager;

import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.enums.AstralObjectType;
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

        // Check if the selected Planet is already in database. If not, store it ! :)
        Planet planet = (Planet) astralObjectService.findOneByName(jsonPlanet.getName());
        if(planet == null) {
            planet = (Planet) astralObjectService.create(jsonPlanet.getName(), jsonPlanet.getParent(), AstralObjectType.PLANET);
        }
        return planet;
    }
}
