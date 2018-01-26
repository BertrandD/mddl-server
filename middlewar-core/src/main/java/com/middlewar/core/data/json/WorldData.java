package com.middlewar.core.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import com.middlewar.core.utils.Rnd;
import lombok.NoArgsConstructor;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * @author bertrand.
 */
@Slf4j
@Component
@NoArgsConstructor
public class WorldData {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${middlewar.data.world.location}")
    private String fileLocation;

    private AstralObject blackHole;
    private List<AstralObject> objects = new ArrayList<>();

    @PostConstruct
    public void init() {
        parseConfigFile(fileLocation);
        log.info("Loaded " + objects.size() + " astral objects");
    }

    private void parseConfigFile(final String configFile) {
        log.info("Loading World data file : " + configFile);
        final ConfigParser parser = new ConfigParser(configFile);
        blackHole = computeData(parser.getData(), null);
    }

    @SuppressWarnings("unchecked")
    private AstralObject computeData(final Map<String, Object> data, final AstralObject parent) {
        final AstralObject astralObject;
        final String name = (String) data.getOrDefault("name", "Universe Zero");
        final String type = (String) data.getOrDefault("type", "BlackHole");

        try {
            final Constructor<?> constructor = Class.forName("com.middlewar.core.model.space." + type).getConstructor(String.class, AstralObject.class);
            astralObject = (AstralObject) constructor.newInstance(name, parent);
            objects.add(astralObject);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
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
        return blackHole;
    }

    public List<AstralObject> getUnivers() {
        return this.objects;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getWithFilter(Predicate<? super AstralObject> filter) {
        return (List<T>) this.objects.stream().filter(filter).collect(toList());
    }

    public Planet getRandomPlanet() {
        Star star = null;
        int planetCnt = 0;
        int retryCount = 0;
        final int MAX_RETRY = 1000;

        while (planetCnt == 0 && retryCount < MAX_RETRY) {
            // star = getRandomStar();
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
