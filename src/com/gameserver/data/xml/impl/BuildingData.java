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
import org.apache.log4j.Logger;
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

    private final Logger LOGGER = Logger.getLogger(getClass().getSimpleName());
    private final HashMap<String, Building> _buildings = new HashMap<>();

    protected BuildingData(){
        load();
    }

    @Override
    public void load() {
        _buildings.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/buildings", false);
        LOGGER.info("Loaded " + _buildings.size() + " buildings Templates.");
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

                        for(int i = 0; i < attrs.getLength(); i++) {
                            final Node att = attrs.item(i);
                            set.set(att.getNodeName(), att.getNodeValue());
                        }

                        final Building building = createBuildingObject(set);
                        if(building == null) {
                            LOGGER.info(getClass().getSimpleName()+ " : building cannot be instanciated because building type not recognized => null");
                            continue;
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
