package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import com.middlewar.core.serializer.ShipSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Document(collection = "ships")
@JsonSerialize(using = ShipSerializer.class)
public class Ship extends Vehicle {

    private String structureId;
    private List<String> cargoIds;
    private List<String> engineIds;
    private List<String> moduleIds;
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
        setId(new ObjectId().toString());
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
