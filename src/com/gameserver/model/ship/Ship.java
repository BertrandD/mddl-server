package com.gameserver.model.ship;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Ship implements IShip {

    private String structureId;
    private List<String> cargoIds;
    private List<String> engineIds;
    private List<String> modulesIds;
    private List<String> weaponIds;

    public Ship() {
        setCargoIds(new ArrayList<>());
        setEngineIds(new ArrayList<>());
        setModulesIds(new ArrayList<>());
        setWeaponIds(new ArrayList<>());
    }

    public Ship(String structureId) {
        setStructureId(structureId);
        setCargoIds(new ArrayList<>());
        setEngineIds(new ArrayList<>());
        setModulesIds(new ArrayList<>());
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

    public void setModulesIds(List<String> modulesIds) {
        this.modulesIds = modulesIds;
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
        return ItemData.getInstance().getModules(this.modulesIds);
    }

    @Override
    public List<Weapon> getWeapons() {
        return ItemData.getInstance().getWeapons(this.weaponIds);
    }

    @Override
    public double getDamage() {
        // TODO: Consider modules
        return getWeapons().stream().mapToDouble(Weapon::getDamage).sum();
    }

    @Override
    public double getShield() {
        return 0;
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public double getVolume() {
        // TODO: Consider modules
        return getStructure().getVolume();
    }

    @Override
    public double getWeight() {
        // TODO: Consider modules
        return getStructure().getWeight() +
                getEngines().stream().mapToDouble(Engine::getWeight).sum() +
                getModules().stream().mapToDouble(Module::getWeight).sum() +
                getWeapons().stream().mapToDouble(Weapon::getWeight).sum() +
                getCargos().stream().mapToDouble(Cargo::getWeight).sum();
    }

    @Override
    public double getMaxStorableVolume() {
        // TODO: Consider modules
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum(); // TODO: change capacity for capacity volume
    }

    @Override
    public double getMaxStorableWeight() {
        // TODO: Consider modules
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum(); // TODO: change capacity for capacity weight
    }
}
