package com.middlewar.core.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.core.config.Config;
import com.middlewar.core.model.space.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand.
 */
public class WorldData {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());

    private final List<AstralObject> _astralObjects = new ArrayList<>();
    private AstralObject _blackHole;

    protected WorldData() {
        _astralObjects.clear();
        parseConfigFile(Config.DATA_ROOT_DIRECTORY + "world.json");
        LOGGER.info("Loaded " + _astralObjects.size() + " astral objects");
    }

    private void parseConfigFile(String configFile) {
        LOGGER.info("Loading World data file : " + configFile);

        final ConfigParser parser = new ConfigParser(configFile);

        _blackHole = computeData(parser.getData(), null);
    }

    @SuppressWarnings("unchecked")
    private AstralObject computeData(HashMap<String, Object> data, AstralObject parent) {
        AstralObject astralObject;
        String name = (String)data.get("name");
        switch ((String)data.get("type")) {
            case "BlackHole":
                astralObject = new BlackHole(name);
                _astralObjects.add(astralObject);
                break;
            case "Planet":
                astralObject = new Planet(name, parent);
                _astralObjects.add(astralObject);
                break;
            case "Star":
                astralObject = new Star(name, parent);
                _astralObjects.add(astralObject);
                break;
            case "Moon":
                astralObject = new Moon(name, parent);
                _astralObjects.add(astralObject);
                break;
            default:
                return null;
        }

        HashMap<String, Number> stats = (HashMap<String, Number>) data.get("stats");

        astralObject.setOrbit(stats.get("orbit").doubleValue());
        astralObject.setRevolution(stats.get("revolution").doubleValue());
        astralObject.setSize(stats.get("size").doubleValue());

        if (data.containsKey("satellites")) {
            for(HashMap<String, Object> satellite: (ArrayList<HashMap<String, Object>>)data.get("satellites")) {
                astralObject.getSatellites().add(computeData(satellite, astralObject));
            }
        }

        return astralObject;
    }

    public AstralObject getWorld() {
        return _blackHole;
    }

    public static WorldData getInstance() {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder {
        protected static final WorldData _instance = new WorldData();
    }

    private class ConfigParser {

        private HashMap<String, Object> _configData;

        @SuppressWarnings("unchecked")
        ConfigParser(String fileName) {
            try {
                File f = new File(fileName);
                InputStream file = f.exists() ? new FileInputStream(f) : getClass().getResourceAsStream(fileName);
                ObjectMapper mapper = new ObjectMapper();

                _configData = mapper.readValue(file, HashMap.class);
            } catch (IOException e) {
                LOGGER.error("World data cannot be loaded. Parse error on file : " + fileName);
                e.printStackTrace();
            }
        }

        HashMap<String, Object> getData() {
            return _configData;
        }
    }
}
