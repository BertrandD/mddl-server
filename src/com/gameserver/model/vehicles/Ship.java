package com.gameserver.model.vehicles;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.VehicleState;
import com.gameserver.model.Base;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.serializer.ShipSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
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

    public void setStructureId(String structureId) {
        this.structureId = structureId;
    }

    public void setCargoIds(List<String> cargoIds) {
        this.cargoIds = cargoIds;
    }

    public void setEngineIds(List<String> engineIds) {
        this.engineIds = engineIds;
    }

    public void setModuleIds(List<String> modulesIds) {
        this.moduleIds = modulesIds;
    }

    public void setWeaponIds(List<String> weaponIds) {
        this.weaponIds = weaponIds;
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
