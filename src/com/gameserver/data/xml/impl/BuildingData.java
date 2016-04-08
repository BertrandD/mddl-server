package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingType;
import com.gameserver.enums.ItemType;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.HeadQuarter;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.inventory.InventoryFilter;
import com.util.data.xml.IXmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
public class BuildingData implements IXmlReader {

    private final HashMap<String, Building> _buildings = new HashMap<>();

    protected BuildingData(){
        load();
    }

    @Override
    public void load() {
        _buildings.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/buildings", false);
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _buildings.size() + " buildings Templates.");
    }

    @Override
    public void parseDocument(Document doc)
    {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling())
        {
            if ("list".equalsIgnoreCase(a.getNodeName()))
            {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling())
                {
                    if ("building".equalsIgnoreCase(b.getNodeName()))
                    {
                        NamedNodeMap attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();

                        for(int i = 0; i < attrs.getLength(); i++)
                        {
                            final Node att = attrs.item(i);
                            set.set(att.getNodeName(), att.getNodeValue());
                        }

                        final Building building = createBuildingObject(set);
                        if(building == null) {
                            LOGGER.info(getClass().getSimpleName()+ " : building cannot be instanciated because building type isnt recognized => null");
                            continue;
                        }

                        for(Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            if("creations".equalsIgnoreCase(c.getNodeName()))
                            {
                                final HashMap<Integer, Requirement> creations = new HashMap<>();
                                for(Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    if("creation".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        attrs = d.getAttributes();
                                        final int creationLevel = parseInteger(attrs, "level");
                                        final Requirement requirement = new Requirement();

                                        for(Node e = d.getFirstChild(); e != null ; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("item".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                requirement.addItem(new ItemHolder(parseString(attrs, "id"), parseLong(attrs, "count", 0L)));
                                            }
                                            else if("building".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                requirement.addBuilding(new BuildingHolder(parseString(attrs, "id"), parseInteger(attrs, "level", 1)));
                                            }
                                            else if("technology".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                // TODO: Technology
                                                // requirement.addTechnology(new TechnologyHolder(parseString(attrs, "id"), parseInteger(attrs, "level", 1)));
                                            }
                                        }
                                        creations.put(creationLevel, requirement);
                                    }
                                }
                                building.setRequirements(creations);
                            }
                            else if("properties".equalsIgnoreCase(c.getNodeName()))
                            {
                                for(Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    if("production".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        attrs = d.getAttributes();
                                        ((Mine)building).setProduceItemId(parseString(attrs, "id"));
                                        final HashMap<Integer, Long> productionByLevel = new HashMap<>();
                                        for(Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            if("set".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                attrs = e.getAttributes();
                                                productionByLevel.put(parseInteger(attrs, "level"), parseLong(attrs, "value"));
                                            }
                                        }
                                        ((Mine)building).setProductionByLevel(productionByLevel);
                                    }
                                    else if("storageFilter".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final InventoryFilter filter = new InventoryFilter();
                                        for(Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("item".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                final String id = parseString(attrs, "id");
                                                if(id != null)
                                                {
                                                    filter.getIds().add(id);
                                                }
                                                else
                                                {
                                                    filter.getTypes().add(parseEnum(attrs, ItemType.class, "type"));
                                                }
                                            }
                                        }
                                        ((Storage)building).setFilter(filter);
                                    }
                                    else if("maxStorage".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final HashMap<Integer, Long> maxStorage = new HashMap<>();
                                        for(Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("set".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                maxStorage.put(parseInteger(attrs, "level"), parseLong(attrs, "value"));
                                            }
                                        }

                                        ((Storage)building).setCapacityByLevel(maxStorage);
                                    }
                                }
                            }
                        }

                        _buildings.put(set.getString("id"), building);
                    }
                }
            }
        }
    }

    private Building createBuildingObject(StatsSet set)
    {
        switch (set.getEnum("type", BuildingType.class))
        {
            case HEADQUARTER: return new HeadQuarter(set);
            case STORAGE: return new Storage(set);
            case MINE: return new Mine(set);
        }
        return null;
    }

    public Building getBuilding(String id){
        return _buildings.get(id);
    }

    public List<Building> getBuildings(BuildingType type){
        return _buildings.values().stream().filter(k -> k.getType().equals(type)).collect(Collectors.toList());
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
