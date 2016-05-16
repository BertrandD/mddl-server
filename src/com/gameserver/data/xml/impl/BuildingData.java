package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.util.data.xml.IXmlReader;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
public class BuildingData implements IXmlReader {

    private final Logger LOGGER = Logger.getLogger(getClass().getSimpleName());
    private final HashMap<String, Building> _buildings = new HashMap<>();

    protected BuildingData(){
        load();
    }

    @Override
    public synchronized void load() {
        _buildings.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/buildings", false);
        LOGGER.info("Loaded " + _buildings.size() + " buildings Templates.");
    }

    @Override
    public void parseDocument(Document doc) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling())
        {
            if ("list".equalsIgnoreCase(a.getNodeName()))
            {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling())
                {
                    if ("building".equalsIgnoreCase(b.getNodeName())) {
                        NamedNodeMap attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();
                        final HashMap<Integer, Requirement> requirements = new HashMap<>();

                        for (int i = 0; i < attrs.getLength(); i++) {
                            final Node att = attrs.item(i);
                            set.set(att.getNodeName(), att.getNodeValue());
                        }

                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling()) {
                            if ("requirements".equalsIgnoreCase(c.getNodeName())) {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    attrs = d.getAttributes();
                                    if ("requirement".equalsIgnoreCase(d.getNodeName())) {
                                        final int level = parseInteger(attrs, "level");
                                        final List<FuncHolder> functionHolders = new ArrayList<>();
                                        final List<BuildingHolder> buildingHolders = new ArrayList<>();
                                        final List<ItemHolder> itemHolders = new ArrayList<>();
                                        final HashMap<String, Long> resourceHolders = new HashMap<>();

                                        for (Node e = d.getFirstChild(); e != null; e = e.getNextSibling()) {
                                            attrs = e.getAttributes();
                                            if ("function".equalsIgnoreCase(e.getNodeName())) {
                                                functionHolders.add(new FuncHolder(parseString(attrs, "id"), parseString(attrs, "val")));
                                            } else if ("item".equalsIgnoreCase(e.getNodeName())) {
                                                itemHolders.add(new ItemHolder(parseString(attrs, "id"), parseLong(attrs, "count")));
                                            } else if ("building".equalsIgnoreCase(e.getNodeName())) {
                                                buildingHolders.add(new BuildingHolder(parseString(attrs, "id"), parseInteger(attrs, "level")));
                                            } else if ("resource".equalsIgnoreCase(e.getNodeName())) {
                                                resourceHolders.put(parseString(attrs, "id"), parseLong(attrs, "count"));
                                            } else if ("technology".equalsIgnoreCase(e.getNodeName())) {
                                                LOGGER.info("Technology requirement cannot be parsed: TODO."); // TODO
                                            }
                                        }
                                        requirements.put(level, new Requirement(level, functionHolders, itemHolders, buildingHolders, resourceHolders));
                                    }
                                }
                            } else if ("properties".equalsIgnoreCase(c.getNodeName())) {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    attrs = d.getAttributes();
                                    if ("property".equalsIgnoreCase(d.getNodeName())) {
                                        final String name = parseString(attrs, "name");
                                        final String value = parseString(attrs, "value");
                                        set.set(name, value);
                                    }
                                }
                            }
                        }

                        final Building building;
                        try {
                            building = makeBuilding(set);
                            if (building != null) {
                                building.setRequirements(requirements);
                                _buildings.put(set.getString("id"), building);
                            }
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private Building makeBuilding(StatsSet set) throws InvocationTargetException {
        try
        {
            final Constructor<?> c = Class.forName("com.gameserver.model.buildings." + set.getString("type")).getConstructor(StatsSet.class);
            return (Building) c.newInstance(set);
        }
        catch (Exception e)
        {
            throw new InvocationTargetException(e);
        }
    }

    public Building getBuilding(String id) {
        return _buildings.get(id);
    }

    public List<Building> getBuildings(BuildingCategory type){
        return _buildings.values().stream().filter(k -> k.getType().equals(type)).collect(Collectors.toList());
    }

    public List<Building> getBuildings(){
        return new ArrayList<>(_buildings.values());
    }

    public List<Building> getBuildings(Lang lang){
        final List<Building> buildings = new ArrayList<>(_buildings.values());
        buildings.forEach(k->k.setLang(lang));
        return buildings;
    }

    public static BuildingData getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final BuildingData _instance = new BuildingData();
    }
}
