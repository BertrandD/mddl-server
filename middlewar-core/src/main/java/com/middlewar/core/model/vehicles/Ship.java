package com.middlewar.core.model.vehicles;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
public class Ship extends Vehicle {

    private String structureId;
    @ElementCollection
    private List<String> cargoIds;
    @ElementCollection
    private List<String> engineIds;
    @ElementCollection
    private List<String> moduleIds;
    @ElementCollection
    private List<String> weaponIds;

    public Ship() {
        super();
        setCargoIds(new ArrayList<>());
        setEngineIds(new ArrayList<>());
        setModuleIds(new ArrayList<>());
        setWeaponIds(new ArrayList<>());
    }

    public Ship(Base base, String structureId, long count) {
        super();
        setBase(base);
        setStructureId(structureId);
        setCount(count);
        setState(VehicleState.BASED);
        setCargoIds(new ArrayList<>());
        setEngineIds(new ArrayList<>());
        setModuleIds(new ArrayList<>());
        setWeaponIds(new ArrayList<>());
    }

    @Override
    public Structure getStructure() {
        return ItemData.getInstance().getStructure(this.structureId);
    }

    @Override
    public List<Cargo> getCargos() {
        return ItemData.getInstance().getCargos(this.cargoIds);
    }

    @Override
    public List<Engine> getEngines() {
        return ItemData.getInstance().getEngines(this.engineIds);
    }

    @Override
    public List<Module> getModules() {
        return ItemData.getInstance().getModules(this.moduleIds);
    }

    @Override
    public List<Weapon> getWeapons() {
        return ItemData.getInstance().getWeapons(this.weaponIds);
    }
}
