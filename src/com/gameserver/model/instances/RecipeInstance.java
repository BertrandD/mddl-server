package com.gameserver.model.instances;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Player;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "recipes")
public class RecipeInstance {

    @Id
    private String id;
    private String name;

    @DBRef
    private Player owner;
    private String structureId;
    private List<String> cargos;
    private List<String> engines;
    private List<String> modules;
    private List<String> weapons;

    public RecipeInstance(){}

    public RecipeInstance(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> technologies, List<String> weapons){
        setName(name);
        setOwner(owner);
        setStructureId(structureId);
        setCargos(cargos);
        setEngines(engines);
        setModules(modules);
        setWeapons(weapons);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Structure getStructure() {
        return ItemData.getInstance().getStructure(structureId);
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public ArrayList<Cargo> getCargos() {
        final ArrayList<Cargo> res = new ArrayList<>();
        cargos.forEach(k -> res.add(ItemData.getInstance().getCargo(k)));
        return res;
    }

    public void setCargos(List<String> cargos) {
        this.cargos = cargos;
    }

    public ArrayList<Engine> getEngines() {
        final ArrayList<Engine> res = new ArrayList<>();
        engines.forEach(k -> res.add(ItemData.getInstance().getEngine(k)));
        return res;
    }

    public void setEngines(List<String> engines) {
        this.engines = engines;
    }

    public ArrayList<Module> getModules() {
        final ArrayList<Module> res = new ArrayList<>();
        modules.forEach(k -> res.add(ItemData.getInstance().getModule(k)));
        return res;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public ArrayList<Weapon> getWeapons() {
        final ArrayList<Weapon> res = new ArrayList<>();
        weapons.forEach(k -> res.add(ItemData.getInstance().getWeapon(k)));
        return res;
    }

    public void setWeapons(List<String> weapons) {
        this.weapons = weapons;
    }
}
