package com.middlewar.core.data.xml;

import com.middlewar.core.config.Config;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.enums.Rank;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.ItemHolder;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.interfaces.IXmlReader;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.CommonItem;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import com.middlewar.core.model.stats.Stats;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Slf4j
public class ItemData implements IXmlReader {

    private final HashMap<String, GameItem> _all = new HashMap<>();
    private final HashMap<String, Structure> _structures = new HashMap<>();
    private final HashMap<String, Cargo> _cargos = new HashMap<>();
    private final HashMap<String, Engine> _engines = new HashMap<>();
    private final HashMap<String, Module> _modules = new HashMap<>();
    private final HashMap<String, Weapon> _weapons = new HashMap<>();
    private final HashMap<String, CommonItem> _resources = new HashMap<>();
    private final HashMap<String, CommonItem> _commons = new HashMap<>();

    protected ItemData() {
        load();
    }

    public static ItemData getInstance() {
        return SingletonHolder._instance;
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

        parseDirectory(new File("classpath:/data/stats/items"), true);

        log.info("Loaded " + _structures.size() + " structures templates.");
        log.info("Loaded " + _cargos.size() + " cargos templates.");
        log.info("Loaded " + _engines.size() + " engines templates.");
        log.info("Loaded " + _modules.size() + " modules templates.");
        log.info("Loaded " + _weapons.size() + " weapons templates.");
        log.info("Loaded " + _resources.size() + " resources templates.");
        log.info("Loaded " + _commons.size() + " commons templates.");
        log.info("Loaded " + _all.size() + " items in total.");
    }

    @Override
    public void parseDocument(Document doc, File f) {
        for (Node a = doc.getFirstChild(); a != null; a = a.getNextSibling()) {
            if ("list".equalsIgnoreCase(a.getNodeName())) {
                for (Node b = a.getFirstChild(); b != null; b = b.getNextSibling()) {
                    if ("item".equalsIgnoreCase(b.getNodeName())) {
                        NamedNodeMap attrs = b.getAttributes();
                        final StatsSet set = new StatsSet();

                        set.set("id", parseString(attrs, "id"));
                        if (_all.containsKey(set.getString("id"))) continue;

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
                        final List<StatHolder> stats = new ArrayList<>();

                        for (Node c = b.getFirstChild(); c != null; c = c.getNextSibling()) {
                            if ("requirements".equalsIgnoreCase(c.getNodeName())) {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    attrs = d.getAttributes();
                                    if ("item".equalsIgnoreCase(d.getNodeName())) {
                                        final String itemId = parseString(attrs, "id");
                                        final long itemCount = parseLong(attrs, "count");
                                        itemHolders.add(new ItemHolder(itemId, itemCount));
                                    } else if ("building".equalsIgnoreCase(d.getNodeName())) {
                                        final String buildingId = parseString(attrs, "id");
                                        final int buildingLevel = parseInteger(attrs, "level");
                                        buildingHolders.add(new BuildingHolder(buildingId, buildingLevel));
                                    }
                                }
                            } else if ("stats".equalsIgnoreCase(c.getNodeName())) {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    attrs = d.getAttributes();
                                    if ("stat".equalsIgnoreCase(d.getNodeName())) {
                                        final Stats stat = parseEnum(attrs, Stats.class, "name", Stats.NONE);
                                        final StatOp op = parseEnum(attrs, StatOp.class, "op", StatOp.DIFF);
                                        final double value = parseDouble(attrs, "value", 0.0);
                                        final StatHolder holder = new StatHolder(stat, value, op);

                                        stats.add(holder);
                                    }
                                }
                            } else if ("properties".equalsIgnoreCase(c.getNodeName())) {
                                for (Node d = c.getFirstChild(); d != null; d = d.getNextSibling()) {
                                    if ("set".equalsIgnoreCase(d.getNodeName())) {
                                        attrs = d.getAttributes();
                                        set.set(parseString(attrs, "name"), parseString(attrs, "value"));
                                    }
                                }
                            }
                        }
                        makeItem(set, new Requirement(itemHolders, buildingHolders), stats);
                    }
                }
            }
        }
    }

    private void makeItem(StatsSet set, Requirement requirement, List<StatHolder> stats) {
        final String id = set.getString("id");
        final ItemType type = set.getEnum("type", ItemType.class, ItemType.NONE);
        GameItem item = null;
        switch (type.name().toLowerCase()) {
            case "resource": {
                item = new CommonItem(set);
                _resources.put(id, (CommonItem) item);
                break;
            }
            case "common": {
                item = new CommonItem(set);
                _commons.put(id, (CommonItem) item);
                break;
            }
            case "cargo": {
                item = new Cargo(set, requirement);
                _cargos.put(id, (Cargo) item);
                break;
            }
            case "engine": {
                item = new Engine(set, requirement);
                _engines.put(id, (Engine) item);
                break;
            }
            case "module": {
                item = new Module(set, requirement);
                _modules.put(id, (Module) item);
                break;
            }
            case "structure": {
                item = new Structure(set, requirement);
                _structures.put(id, (Structure) item);
                break;
            }
            case "weapon": {
                item = new Weapon(set, requirement);
                _weapons.put(id, (Weapon) item);
                break;
            }
        }

        if (item != null) {
            item.addStats(stats);
            _all.put(id, item);
        }
    }

    public List<CommonItem> getResources(Lang lang) {
        _resources.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_resources.values());
    }

    public List<CommonItem> getCommonItems(Lang lang) {
        _commons.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_commons.values());
    }

    public List<Cargo> getCargos(Lang lang) {
        _cargos.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_cargos.values());
    }

    public List<Engine> getEngines(Lang lang) {
        _engines.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_engines.values());
    }

    public List<Module> getModules(Lang lang) {
        _modules.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_modules.values());
    }

    public List<Weapon> getWeapons(Lang lang) {
        _weapons.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_weapons.values());
    }

    public List<Structure> getStructures(Lang lang) {
        _structures.values().forEach(k -> k.setLang(lang));
        return new ArrayList<>(_structures.values());
    }

    public List<CommonItem> getResources() {
        return new ArrayList<>(_resources.values());
    }

    public List<CommonItem> getCommonItems() {
        return new ArrayList<>(_commons.values());
    }

    public List<Cargo> getCargos() {
        return new ArrayList<>(_cargos.values());
    }

    public List<Engine> getEngines() {
        return new ArrayList<>(_engines.values());
    }

    public List<Module> getModules() {
        return new ArrayList<>(_modules.values());
    }

    public List<Weapon> getWeapons() {
        return new ArrayList<>(_weapons.values());
    }

    public List<Structure> getStructures() {
        return new ArrayList<>(_structures.values());
    }

    public CommonItem getResource(String id) {
        return _resources.get(id);
    }

    public CommonItem getCommonItem(String id) {
        return _commons.get(id);
    }

    public Structure getStructure(String id) {
        return _structures.get(id);
    }

    public Cargo getCargo(String id) {
        return _cargos.get(id);
    }

    public Engine getEngine(String id) {
        return _engines.get(id);
    }

    public Module getModule(String id) {
        return _modules.get(id);
    }

    public Weapon getWeapon(String id) {
        return _weapons.get(id);
    }

    public GameItem getTemplate(String itemId) {
        return _all.get(itemId);
    }

    public GameItem getTemplate(String itemId, Lang lang) {
        final GameItem item = _all.get(itemId);
        if (item == null) return null;
        item.setLang(lang);
        return item;
    }

    public List<Cargo> getCargos(List<String> ids) {
        final List<Cargo> res = new ArrayList<>();
        ids.forEach(k -> res.add(getCargo(k)));
        return res;
    }

    public List<Engine> getEngines(List<String> ids) {
        final List<Engine> res = new ArrayList<>();
        ids.forEach(k -> res.add(getEngine(k)));
        return res;
    }

    public List<Module> getModules(List<String> ids) {
        final List<Module> res = new ArrayList<>();
        ids.forEach(k -> res.add(getModule(k)));
        return res;
    }

    public List<Weapon> getWeapons(List<String> ids) {
        final List<Weapon> res = new ArrayList<>();
        ids.forEach(k -> res.add(getWeapon(k)));
        return res;
    }

    public int getSize() {
        return _all.size();
    }

    private static class SingletonHolder {
        protected static final ItemData _instance = new ItemData();
    }
}
