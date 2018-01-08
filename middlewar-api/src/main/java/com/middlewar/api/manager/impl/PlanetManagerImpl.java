package com.middlewar.api.manager.impl;

import com.middlewar.api.manager.PlanetManager;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class PlanetManagerImpl implements PlanetManager {

    //@Autowired
    //private AstralObjectService astralObjectService;

    @Autowired
    private WorldData worldData;

    public Planet pickRandom() {
        // Pick a random planet from universe
        final Planet jsonPlanet = worldData.getRandomPlanet();

        //return (Planet) astralObjectService.findOneByName(jsonPlanet.getName());
        return null; // TODO
    }
}
