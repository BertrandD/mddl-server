package com.gameserver.data.xml.impl;

import com.config.Config;
import com.gameserver.enums.ItemType;
import com.gameserver.enums.Lang;
import com.gameserver.enums.Rank;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.gameserver.interfaces.IXmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOGGER = LoggerFactory.getLogger(getClass().getSimpleName());
    private final HashMap<String, GameItem> _all = new HashMap<>();
    private final HashMap<String, Structure> _structures = new HashMap<>();
    private final HashMap<String, Cargo> _cargos = new HashMap<>();
    private final HashMap<String, Engine> _engines = new HashMap<>();
    private final HashMap<String, Module> _modules = new HashMap<>();
    private final HashMap<String, Weapon> _weapons = new HashMap<>();
    private final HashMap<String, CommonItem> _resources = new HashMap<>();
    private final HashMap<String, CommonItem> _commons = new HashMap<>();

    protected ItemData(){
        load();
    }

    @Override
    public synchronized void load() {
        _all.clear();
        _structures.clear();
        _cargos.clear();
        _engines.clear();
        _modules.clear();
        _weapons.clear();
        _resources.clear();
        _commons.clear();
        parseDatapackDirectory(Config.DATA_ROOT_DIRECTORY + "stats/items", false);
        LOGGER.info("Loaded " + _structures.size() + " structures templates.");
        LOGGER.info("Loaded " + _cargos.size() + " cargos templates.");
        LOGGER.info("Loaded " + _engines.size() + " engines templates.");
        LOGGER.info("Loaded " + _modules.size() + " modules templates.");
        LOGGER.info("Loaded " + _weapons.size() + " weapons templates.");
        LOGGER.info("Loaded "+ _resources.size() + " resources templates.");
        LOGGER.info("Loaded " + _commons.size() + " commons templates.");
        LOGGER.info("Loaded " + _all.size() + " items in total.");
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
                    if ("item".equalsIgnoreCase(b.getNodeName()))
                    {
                        NamedNodeMap attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();

                        set.set("id", parseString(attrs, "id"));
                        if(_all.containsKey(set.getString("id"))) continue;

                        set.set("type", parseEnum(attrs, ItemType.class, "type"));
                        set.set("nameId", parseString(attrs, "nameId"));
                        set.set("rank", parseEnum(attrs, Rank.class, "rank", Rank.NONE));
                        set.set("weight", parseLong(attrs, "weight", 0L));
                        set.set("volume", parseLong(attrs, "volume", 0L));
                        set.set("descriptionId", parseString(attrs, "descriptionId"));
                        set.set("buildTime", parseLong(attrs, "buildTime"));

                        // Requirements Holders
                        final List<BuildingHolder> buildingHolders = new ArrayList<>();
                        final List<ItemHolder> itemHolders = new ArrayList<>();

                        for(Node c = b.getFirstChild(); c != null; c = c.getNextSibling())
                        {
                            if("requirements".equalsIgnoreCase(c.getNodeName()))
                            {
                                for(Node d = c.getFirstChild(); d != null ; d = d.getNextSibling())
                                {
                                    attrs = d.getAttributes();
                                    if("item".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final String itemId = parseString(attrs, "id");
                                        final long itemCount = parseLong(attrs, "count");
                                        itemHolders.add(new ItemHolder(itemId, itemCount));
                                    }
                                    else if("building".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        final String buildingId = parseString(attrs, "id");
                                        final int buildingLevel = parseInteger(attrs, "level");
                                        buildingHolders.add(new BuildingHolder(buildingId, buildingLevel));
                                    }
                                }
                            }
                            else if("properties".equalsIgnoreCase(c.getNodeName()))
                            {
                                for(Node d = c.getFirstChild(); d != null; d = d.getNextSibling())
                                {
                                    if("set".equalsIgnoreCase(d.getNodeName()))
                                    {
                                        attrs = d.getAttributes();
                                        set.set(parseString(attrs, "name"), parseString(attrs, "value"));
                                    }
                                }
                            }
                        }
                        makeItem(set, new Requirement(itemHolders, buildingHolders));
                    }
                }
            }
        }
    }

    private void makeItem(StatsSet set, Requirement requirement)
    {
        final String id = set.getString("id");
        final ItemType type = set.getEnum("type", ItemType.class, ItemType.NONE);
        GameItem item = null;
        switch(type.name().toLowerCase())
        {
            case "resource":
            {
                item = new CommonItem(set);
                _resources.put(id, (CommonItem)item); break;
            }
            case "common":
            {
                item = new CommonItem(set);
                _commons.put(id, (CommonItem)item); break;
            }
            case "cargo":
            {
                item = new Cargo(set, requirement);
                _cargos.put(id, (Cargo)item); break;
            }
            case "engine":
            {
                item = new Engine(set, requirement);
                _engines.put(id, (Engine)item); break;
            }
            case "module":
            {
                item = new Module(set, requirement);
                _modules.put(id, (Module)item); break;
            }
            case "structure":
            {
                item = new Structure(set, requirement);
                _structures.put(id, (Structure)item); break;
            }
            case "weapon":
            {
                item = new Weapon(set, requirement);
                _weapons.put(id, (Weapon)item); break;
            }
        }
        if(item != null) _all.put(id, item);
    }

    public List<CommonItem> getResources(Lang lang) {
        _resources.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_resources.values());
    }

    public List<CommonItem> getCommonItems(Lang lang) {
        _commons.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_commons.values());
    }

    public List<Cargo> getCargos(Lang lang) {
        _cargos.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_cargos.values());
    }

    public List<Engine> getEngines(Lang lang) {
        _engines.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_engines.values());
    }

    public List<Module> getModules(Lang lang) {
        _modules.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_modules.values());
    }

    public List<Weapon> getWeapons(Lang lang) {
        _weapons.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>(_weapons.values());
    }

    public List<Structure> getStructures(Lang lang) {
        _structures.values().stream().forEach(k->k.setLang(lang));
        return new ArrayList<>( _structures.values());
    }

    public List<CommonItem> getResources() { return new ArrayList<>(_resources.values()); }

    public List<CommonItem> getCommonItems() { return new ArrayList<>(_commons.values()); }

    public List<Cargo> getCargos() { return new ArrayList<>(_cargos.values()); }

    public List<Engine> getEngines() { return new ArrayList<>(_engines.values()); }

    public List<Module> getModules() { return new ArrayList<>(_modules.values()); }

    public List<Weapon> getWeapons() { return new ArrayList<>(_weapons.values()); }

    public List<Structure> getStructures() { return new ArrayList<>( _structures.values()); }

    public CommonItem getResource(String id) { return _resources.get(id); }

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

    public GameItem getTemplate(String itemId){
       return _all.get(itemId);
    }

    public GameItem getTemplate(String itemId, Lang lang){
        final GameItem item = _all.get(itemId);
        if(item == null) return null;
        item.setLang(lang);
        return item;
    }

    public int getSize() {
        return _all.size();
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
