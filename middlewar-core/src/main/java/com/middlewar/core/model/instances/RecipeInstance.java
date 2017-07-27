package com.middlewar.core.model.instances;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@NoArgsConstructor
public class RecipeInstance {

    @Id
    private long id;
    private String name;

    @ManyToOne
    private Player owner;
    private String structureId;
    @ElementCollection
    private List<String> cargos;
    @ElementCollection
    private List<String> engines;
    @ElementCollection
    private List<String> modules;
    @ElementCollection
    private List<String> weapons;

    public RecipeInstance(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> technologies, List<String> weapons) {
        setName(name);
        setOwner(owner);
        setStructureId(structureId);
        setCargos(cargos);
        setEngines(engines);
        setModules(modules);
        setWeapons(weapons);
    }

    public Structure getStructure() {
        return ItemData.getInstance().getStructure(structureId);
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
