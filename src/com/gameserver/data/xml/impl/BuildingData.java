package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.Lang;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.holders.PropertiesHolder;
import com.gameserver.holders.PropertyHolder;
import com.gameserver.holders.PropertyListHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;
import com.gameserver.interfaces.IXmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());

    private final HashMap<String, Building> _buildings = new HashMap<>();

    protected BuildingData(){
        load();
    }

    @Override
    public synchronized void load() {
        _buildings.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/buildings", true);
        LOGGER.info("Loaded " + _buildings.size() + " buildings Templates.");
    }

    @Override
    public void parseDocument(Document doc) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling())
        {
            if ("list".equalsIgnoreCase(a.getNodeName()))
            {
                NamedNodeMap attrs = a.getAttributes();
                if(parseBoolean(attrs, "disabled", false)) continue;

                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling())
                {
                    if ("building".equalsIgnoreCase(b.getNodeName())) {
                        attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();
                        final HashMap<Integer, Requirement> requirements = new HashMap<>();
                        final HashMap<Integer, Long> buildTimes = new HashMap<>();

                        for (int i = 0; i < attrs.getLength(); i++) {
                            final Node att = attrs.item(i);
                            set.set(att.getNodeName(), att.getNodeValue());
                        }

                        final long[] energies = new long[set.getInt("maxLevel")];

                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            if ("requirements".equalsIgnoreCase(c.getNodeName()))
                            {
                                final List<FunctionHolder> functions = new ArrayList<>();
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if("functions".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        for (Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("function".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                final int fromLevel = parseInteger(attrs, "fromLevel", 1);
                                                final int toLevel = parseInteger(attrs, "toLevel", set.getInt("maxLevel"));
                                                final String itemId = parseString(attrs, "itemId");
                                                final String function = parseString(attrs, "value");
                                                functions.add(new FunctionHolder(fromLevel, toLevel, itemId, function));
                                            }
                                        }
                                    }
                                    else if ("requirement".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final int level = parseInteger(attrs, "level");
                                        final List<BuildingHolder> buildingHolders = new ArrayList<>();
                                        final List<ItemHolder> itemHolders = new ArrayList<>();

                                        for (Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if ("item".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                itemHolders.add(new ItemHolder(parseString(attrs, "id"), parseLong(attrs, "count")));
                                            }
                                            else if ("building".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                buildingHolders.add(new BuildingHolder(parseString(attrs, "id"), parseInteger(attrs, "level")));
                                            }
                                        }
                                        requirements.put(level, new Requirement(level, itemHolders, buildingHolders));
                                    }
                                }

                                // Finishing REQUIREMENTS with items from functions
                                for (FunctionHolder function : functions)
                                {
                                    for(int i = function.getFromLevel(); i <= function.getToLevel(); i++)
                                    {
                                        Requirement req = requirements.get(i);
                                        if(req != null)
                                        {
                                            req.addItem(new ItemHolder(function.getItemId(), function.getResultForLevel(i)));
                                        }
                                        else
                                        {
                                            requirements.put(i, new Requirement(i, new ItemHolder(function.getItemId(), function.getResultForLevel(i))));
                                        }
                                    }
                                }
                            }
                            else if ("buildTime".equalsIgnoreCase(c.getNodeName()))
                            {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if ("set".equalsIgnoreCase(d.getNodeName())) {
                                        final int fromLevel = parseInteger(attrs, "fromLevel", 1);
                                        final int toLevel = parseInteger(attrs, "toLevel", set.getInt("maxLevel"));
                                        final String function = parseString(attrs, "function");

                                        final FunctionHolder holder = new FunctionHolder(fromLevel, toLevel, function);
                                        for(int i = holder.getFromLevel(); i <= holder.getToLevel(); i++) {
                                            buildTimes.put(i, (holder.getResultForLevel(i)));
                                        }
                                    }
                                }
                            }
                            else if ("energy".equalsIgnoreCase(c.getNodeName()))
                            {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if ("set".equalsIgnoreCase(d.getNodeName())) {
                                        final int fromLevel = parseInteger(attrs, "fromLevel", 1);
                                        final int toLevel = parseInteger(attrs, "toLevel", set.getInt("maxLevel"));
                                        final String function = parseString(attrs, "function");

                                        final FunctionHolder holder = new FunctionHolder(fromLevel, toLevel, function);
                                        for(int i = holder.getFromLevel(); i <= holder.getToLevel(); i++) {
                                            energies[i-1] = (holder.getResultForLevel(i));
                                        }
                                    }
                                }
                            }
                            else if ("properties".equalsIgnoreCase(c.getNodeName()))
                            {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if ("property".equalsIgnoreCase(d.getNodeName())) {
                                        final String name = parseString(attrs, "name");
                                        final String value = parseString(attrs, "value");
                                        set.set(name, value);
                                    }
                                }
                            }
                            else if("propertiesByLevel".equalsIgnoreCase(c.getNodeName()))
                            {
                                final PropertiesHolder propertiesHolder = new PropertiesHolder();
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if("properties".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final int level = parseInteger(attrs, "level");
                                        final List<PropertyListHolder> groupHolder = propertiesHolder.addLevelGroup(level);

                                        for(Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
                                        {
                                            attrs = e.getAttributes();
                                            if("list".equalsIgnoreCase(e.getNodeName()))
                                            {
                                                final String listName = parseString(attrs, "name");
                                                final PropertyListHolder propertyList = new PropertyListHolder(listName);
                                                groupHolder.add(propertyList);

                                                for(Node f = e.getFirstChild(); f != null; f = f.getNextSibling())
                                                {
                                                    attrs = f.getAttributes();
                                                    if("set".equalsIgnoreCase(f.getNodeName()))
                                                    {
                                                        final String value = parseString(attrs, "value");
                                                        final String name = parseString(attrs, "name");
                                                        propertyList.addProperty(new PropertyHolder(name, value));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                set.set("propertiesByLevel", propertiesHolder);
                            }
                            else if ("productions".equalsIgnoreCase(c.getNodeName()))
                            {
                                final List<FuncHolder> productions = new ArrayList<>();
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    attrs = d.getAttributes();
                                    if("produce".equalsIgnoreCase(d.getNodeName())) {
                                        final String itemId = parseString(attrs, "itemId");
                                        final String func = parseString(attrs, "function");
                                        productions.add(new FuncHolder(itemId, func));
                                    }
                                }

                                if(!productions.isEmpty()){
                                    set.set("production", productions);
                                }
                            }
                        }

                        final Building building;
                        try {
                            building = makeBuilding(set);
                            if (building != null) {
                                building.setUseEnergy(energies);
                                building.setBuildTimes(buildTimes);
                                building.setAllRequirements(requirements);
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

    private class FunctionHolder
    {
        private int fromLevel;
        private int toLevel;
        private String itemId;
        private String function;

        public FunctionHolder(int fromLevel, int toLevel, String function) {
            this.fromLevel = fromLevel;
            this.toLevel = toLevel;
            this.function = function;
        }

        public FunctionHolder(int fromLevel, int toLevel, String itemId, String function) {
            this.fromLevel = fromLevel;
            this.toLevel = toLevel;
            this.itemId = itemId;
            this.function = function;
        }

        public int getFromLevel() {
            return fromLevel;
        }

        public int getToLevel() {
            return toLevel;
        }

        public String getItemId() {
            return itemId;
        }

        public long getResultForLevel(int level) {
            final String func = function.replace("$level", "" + level);
            return ((Number) Evaluator.getInstance().eval(func)).longValue();
        }
    }

    private Building makeBuilding(StatsSet set) throws InvocationTargetException {
        try {
            final Constructor<?> c = Class.forName("com.gameserver.model.buildings." + set.getString("type")).getConstructor(StatsSet.class);
            return (Building) c.newInstance(set);
        } catch (Exception e) {
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

    /**
     * Used for live reload when Config.SOME_CONFIG has changed.
     * DONT USE LIVE RELOAD ON LIVE SERVERS !!!!!
     */
    public void resetBuildings()
    {
        for (int i = 0; i < getBuildings().size(); i++)
        {
            final Building current = getBuildings().get(i);
            final long[] useEnergy = new long[current.getMaxLevel()];
            final HashMap<Integer, Long> buildTimes = new HashMap<>();


            // Recalculate use of energy
            for (int j = 0; j < current.getUseEnergy().length; j++) {
                useEnergy[j] = (long)(current.getUseEnergyAtLevel(j+1) * Config.USE_ENERGY_MODIFIER);
            }

            // Recalculate build times
            for (int j = 0; j < current.getBuildTimes().values().size(); j++) {
                buildTimes.put(j+1, (long)(current.getBuildTimeAtLevel(j+1) * Config.BUILDTIME_MODIFIER));
            }

            current.setUseEnergy(useEnergy);
            current.setBuildTimes(buildTimes);
        }
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
