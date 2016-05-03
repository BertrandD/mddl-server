package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.HeadQuarter;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
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

                        for(Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            if("requirements".equalsIgnoreCase(c.getNodeName()))
                            {
                                for(Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if("requirement".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final int level = parseInteger(attrs, "level");
                                        final Requirement requirement = new Requirement();
                                        for(Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("function".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                requirement.addFunction(new FuncHolder(parseString(attrs, "id"), parseString(attrs, "val")));
                                            }
                                            else if("item".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                requirement.addItem(new ItemHolder(parseString(attrs, "id"), parseLong(attrs, "count")));
                                            }
                                            else if("building".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                requirement.addBuilding(new BuildingHolder(parseString(attrs, "id"), parseInteger(attrs, "level")));
                                            }
                                            else if("technology".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                LOGGER.info("Technology requirement cannot be parsed: TODO."); // TODO
                                            }
                                        }
                                        building.addRequirements(level, requirement);
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
        switch (set.getEnum("type", BuildingCategory.class))
        {
            case HEADQUARTER: return new HeadQuarter(set);
            case STORAGE: return new Storage(set);
            case PRODUCTION: return new Mine(set);
        }
        return null;
    }

    public Building getBuilding(String id){
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
