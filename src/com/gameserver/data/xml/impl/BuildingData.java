package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingType;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.buildings.Building;
import com.util.data.xml.IXmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class BuildingData implements IXmlReader {

    private final HashMap<BuildingType, Building> _buildings = new HashMap<>();

    protected BuildingData(){
        load();
    }

    @Override
    public void load() {
        _buildings.clear();
        parseDatapackFile(Config.DATA_ROOT_DIRECTORY + "stats/buildings/buildings.xml");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _buildings.size() + " buildings Templates.");
    }

    @Override
    public void parseDocument(Document doc)
    {
        Requirement requirement = null;

        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling())
        {
            if ("list".equalsIgnoreCase(a.getNodeName()))
            {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling())
                {
                    if ("building".equalsIgnoreCase(b.getNodeName()))
                    {
                        NamedNodeMap attrs = b.getAttributes();
                        String id = parseString(attrs, "id", null);
                        BuildingType type = parseEnum(attrs, BuildingType.class, "type");
                        String name = parseString(attrs, "name", null);
                        String description = parseString(attrs, "description", null);
                        int maxHealth = parseInteger(attrs, "maxHealth", -1);
                        int maxLevel = parseInteger(attrs, "maxLevel", -1);
                        long buildTime = parseLong(attrs, "buildTime", -1L); // TODO: Default values

                        for(Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            if("creation".equalsIgnoreCase(c.getNodeName()))
                            {
                                requirement = new Requirement();
                                for(Node d = c.getFirstChild(); d != null ; d = d.getNextSibling())
                                {
                                    NamedNodeMap battrs = d.getAttributes();
                                    if("item".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        String itemId = parseString(battrs, "id", null);
                                        long itemCount = parseLong(battrs, "count", -1L);
                                        requirement.addItem(new ItemHolder(itemId, itemCount));
                                    }
                                    else if("building".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        BuildingType buildingType = parseEnum(battrs, BuildingType.class, "type");
                                        int buildingLevel = parseInteger(battrs, "level", -1);
                                        requirement.addBuilding(new BuildingHolder(buildingType, buildingLevel));
                                    }
                                    // TODO: Technology
                                }
                            }
                        }

                        _buildings.put(type, new Building(id, type, name, description, maxLevel, maxHealth, buildTime, requirement));
                    }
                }
            }
        }
    }

    public Building getBuilding(BuildingType type){
        return _buildings.get(type);
    }

    public List<Building> getBuildings(){
        return new ArrayList<>(_buildings.values());
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
