package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.BuildingType;
import com.gameserver.enums.Rank;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Item;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.util.data.xml.IXmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class ItemData implements IXmlReader {

    private final HashMap<String, Structure> _structures = new HashMap<>();
    private final HashMap<String, Cargo> _cargos = new HashMap<>();
    private final HashMap<String, Engine> _engines = new HashMap<>();
    private final HashMap<String, Module> _modules = new HashMap<>();
    private final HashMap<String, Weapon> _weapons = new HashMap<>();
    private final HashMap<String, CommonItem> _commons = new HashMap<>();

    protected ItemData(){
        load();
    }

    @Override
    public void load() {
        _structures.clear();
        _cargos.clear();
        _engines.clear();
        _modules.clear();
        _weapons.clear();
        _commons.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/items", false);
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _structures.size() + " structures templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _cargos.size() + " cargos templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _engines.size() + " engines templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _modules.size() + " modules templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _weapons.size() + " weapons templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + _commons.size() + " commons templates.");
        LOGGER.info(getClass().getSimpleName() + ": Loaded " + (_structures.size() + _cargos.size() + _engines.size() + _modules.size() + _weapons.size() + _commons.size()) + " items in total.");
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
                    if ("item".equalsIgnoreCase(b.getNodeName()))
                    {
                        NamedNodeMap attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();

                        set.set("id", parseString(attrs, "id"));
                        set.set("type", parseString(attrs, "type"));
                        set.set("name", parseString(attrs, "name"));
                        set.set("rank", parseEnum(attrs, Rank.class, "rank"));
                        set.set("useslot", parseInteger(attrs, "useslot", 0));
                        set.set("weight", parseInteger(attrs, "weight", 0));
                        set.set("description", parseString(attrs, "description"));
                        set.set("sellable", parseBoolean(attrs, "sellable", false));
                        set.set("tradable", parseBoolean(attrs, "tradable", false));
                        set.set("disabled", parseBoolean(attrs, "disabled", false));
                        set.set("buildtime", parseLong(attrs, "buildTime", -1L)); // TODO: Default values

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
                                        String itemId = parseString(battrs, "id");
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
                            else if("properties".equalsIgnoreCase(c.getNodeName()))
                            {
                                for(Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    if("set".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        NamedNodeMap battrs = d.getAttributes();
                                        set.set(parseString(battrs, "name"), parseString(battrs, "value"));
                                    }
                                }
                            }
                        }
                        makeItem(set, requirement);
                    }
                }
            }
        }
    }

    private void makeItem(StatsSet set, Requirement requirement)
    {
        final String id = set.getString("id");
        final String type = set.getString("type");
        switch(type.toLowerCase())
        {
            case "common":
            {
                _commons.put(id, new CommonItem(set, requirement)); break;
            }
            case "cargo":
            {
                _cargos.put(id, new Cargo(set, requirement)); break;
            }
            case "engine":
            {
                _engines.put(id, new Engine(set, requirement)); break;
            }
            case "module":
            {
                _modules.put(id, new Module(set, requirement)); break;
            }
            case "structure":
            {
                _structures.put(id, new Structure(set, requirement)); break;
            }
            case "weapon":
            {
                _weapons.put(id, new Weapon(set, requirement)); break;
            }
        }
    }

    public List<CommonItem> getCommonItems() { return new ArrayList<>(_commons.values()); }

    public List<Cargo> getCargos() { return new ArrayList<>(_cargos.values()); }

    public List<Engine> getEngines(){ return new ArrayList<>(_engines.values()); }

    public List<Module> getModules() { return new ArrayList<>(_modules.values()); }

    public List<Weapon> getWeapons() { return new ArrayList<>(_weapons.values()); }

    public List<Structure> getStructures(){ return new ArrayList<>( _structures.values()); }

    public CommonItem getCommonItem(String id){
        return _commons.get(id);
    }

    public Structure getStructure(String id){
        return _structures.get(id);
    }

    public Cargo getCargo(String id){ return _cargos.get(id); }

    public Engine getEngine(String id){ return _engines.get(id); }

    public Module getModule(String id){ return _modules.get(id); }

    public Weapon getWeapon(String id){ return _weapons.get(id); }

    public Item getTemplate(String itemId){

        Structure structure = _structures.values().stream().filter((k) -> k.getItemId().equals(itemId)).findFirst().orElse(null);
        if(structure != null) return structure;

        Cargo cargo = _cargos.values().stream().filter(k -> k.getItemId().equals(itemId)).findFirst().orElse(null);
        if(cargo != null) return cargo;

        CommonItem common = _commons.values().stream().filter(k -> k.getItemId().equals(itemId)).findFirst().orElse(null);
        if(common != null) return common;

        return null;
    }

    public static ItemData getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final ItemData _instance = new ItemData();
    }
}
