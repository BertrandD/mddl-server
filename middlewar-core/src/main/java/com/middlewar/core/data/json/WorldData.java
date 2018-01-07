package com.middlewar.core.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.BlackHole;
import com.middlewar.core.model.space.Moon;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import com.middlewar.core.utils.Rnd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bertrand.
 */
@Slf4j
@Component
public class WorldData {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${middlewar.data.world.location}")
    private String fileLocation;

    private final List<AstralObject> _astralObjects = new ArrayList<>();

    private AstralObject _blackHole;
    private Map<String, Star> stars = new HashMap<>();
    private Map<String, Planet> planets = new HashMap<>();

    @PostConstruct
    public void reload() {
        _astralObjects.clear();
        parseConfigFile(fileLocation);
        log.info("Loaded " + _astralObjects.size() + " astral objects");
    }

    private void parseConfigFile(String configFile) {
        log.info("Loading World data file : " + configFile);
        final ConfigParser parser = new ConfigParser(configFile);
        _blackHole = computeData(parser.getData(), null);
    }

    @SuppressWarnings("unchecked")
    private AstralObject computeData(Map<String, Object> data, AstralObject parent) {
        AstralObject astralObject;
        String name = (String) data.get("name");
        switch ((String) data.get("type")) {
            case "BlackHole":
                astralObject = new BlackHole(name);
                _astralObjects.add(astralObject);
                break;
            case "Planet":
                astralObject = new Planet(name, parent);
                _astralObjects.add(astralObject);
                planets.put(astralObject.getName(), (Planet) astralObject);
                break;
            case "Star":
                astralObject = new Star(name, parent);
                _astralObjects.add(astralObject);
                stars.put(astralObject.getName(), (Star) astralObject);
                break;
            case "Moon":
                astralObject = new Moon(name, parent);
                _astralObjects.add(astralObject);
                break;
            default:
                return null;
        }

        final HashMap<String, Number> stats = (HashMap<String, Number>) data.get("stats");

        astralObject.setOrbit(stats.get("orbit").doubleValue());
        astralObject.setRevolution(stats.get("revolution").doubleValue());
        astralObject.setSize(stats.get("size").doubleValue());

        if (data.containsKey("satellites")) {
            for (HashMap<String, Object> satellite : (ArrayList<HashMap<String, Object>>) data.get("satellites")) {
                astralObject.getSatellites().add(computeData(satellite, astralObject));
            }
        }

        return astralObject;
    }

    public AstralObject getWorld() {
        return _blackHole;
    }

    public Star getRandomStar() {
        final int starCnt = getWorld().getSatellites().size();
        return (Star) getWorld().getSatellites().get(Rnd.get(0, starCnt - 1));
    }

    public Planet getRandomPlanet() {
        Star star = null;
        int planetCnt = 0;
        int retryCount = 0;
        final int MAX_RETRY = 10;

        while (planetCnt == 0 && retryCount < MAX_RETRY) {
//            star = getRandomStar();
            star = (Star) getWorld().getSatellites().get(1); // TODO : get random star, but don't let this method return null
            planetCnt = star.getSatellites().size();

            if (planetCnt == 0) log.warn("Star " + star.getId() + " has 0 satellites (planets) !");
            retryCount++;
        }

        if (planetCnt == 0 && retryCount == MAX_RETRY) {
            log.error("Cannot find any Planet object ! The server will crash !");
            // TODO: shutdown the server.
            return null;
        }

        return (Planet) star.getSatellites().get(Rnd.get(0, planetCnt - 1));
    }

    public Star getStar(String id) {
        return stars.get(id);
    }

    public Planet getPlanet(String id) {
        return planets.get(id);
    }

    private class ConfigParser {

        private HashMap<String, Object> _configData;

        @SuppressWarnings("unchecked")
        ConfigParser(String fileName) {
            try {
                final File file = resourceLoader.getResource(fileName).getFile();
                InputStream is = file.exists() ? new FileInputStream(file) : getClass().getResourceAsStream(fileName);
                ObjectMapper mapper = new ObjectMapper();

                _configData = mapper.readValue(is, HashMap.class);
            } catch (IOException e) {
                log.error("World data cannot be loaded. Parse error on file : " + fileName);
                e.printStackTrace();
            }
        }

        public Map<String, Object> getData() {
            return _configData;
        }
    }
}
