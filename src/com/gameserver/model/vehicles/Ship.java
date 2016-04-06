package com.gameserver.model.vehicles;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.StructureSlot;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Structure;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * @author LEBOC Philippe
 */
public class Ship extends Vehicle {

    private long count;

    @DBRef
    private Base base;

    private String structure;

    private ArrayList<String> cargos;

    private ArrayList<String> engines;

    private ArrayList<String> modules;

    private ArrayList<String> weapons;

    // TODO: private ArrayList<String> technologies;

    public Ship(){
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    public Ship(Base base, String structure, long count){
        setBase(base);
        setStructure(structure);
        setCount(count);
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    @Override
    public long getMaxSpeed() {
        int power = 0;
        for(String engineId : getEngines())
        {
            final Engine engine = ItemData.getInstance().getEngine(engineId);
            if(engine != null)
                power += engine.getPower();
        }
        return power;
    }

    public boolean addCargo(ItemInstance item){
        final Structure structure = ItemData.getInstance().getStructure(getStructure());
        if(item.isCargo() && !item.getTemplate().isDisabled() && structure != null && (structure.getAvailablesSlots().get(StructureSlot.CARGO) > getCargos().size())){
            getCargos().add(item.getItemId()); // TODO add checks
            return true;
        }
        return false;
    }

    public boolean addEngine(ItemInstance item){
        final Structure structure = ItemData.getInstance().getStructure(getStructure());
        if(item.isEngine() && !item.getTemplate().isDisabled() && structure != null && (structure.getAvailablesSlots().get(StructureSlot.ENGINE) > getCargos().size())){
            getEngines().add(item.getItemId()); // TODO add checks
            return true;
        }
        return false;
    }

    public boolean addModule(ItemInstance item){
        final Structure structure = ItemData.getInstance().getStructure(getStructure());
        if(item.isModule() && !item.getTemplate().isDisabled() && structure != null && (structure.getAvailablesSlots().get(StructureSlot.MODULE) > getCargos().size())){
            getModules().add(item.getItemId()); // TODO add checks
            return true;
        }
        return false;
    }

    public boolean addTechnology(){
        // TODO technology
        return false;
    }

    public boolean addWeapon(ItemInstance item){
        final Structure structure = ItemData.getInstance().getStructure(getStructure());
        if(item.isWeapon() && !item.getTemplate().isDisabled() && structure != null && (structure.getAvailablesSlots().get(StructureSlot.WEAPON) > getCargos().size())){
            getWeapons().add(item.getItemId()); // TODO add checks
            return true;
        }
        return false;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        if(getStructure() == null)
            this.structure = structure;
    }

    public ArrayList<String> getCargos() {
        return cargos;
    }

    public void setCargos(ArrayList<String> cargos) {
        this.cargos = cargos;
    }

    public ArrayList<String> getEngines() {
        return engines;
    }

    public void setEngines(ArrayList<String> engines) {
        this.engines = engines;
    }

    public ArrayList<String> getModules() {
        return modules;
    }

    public void setModules(ArrayList<String> modules) {
        this.modules = modules;
    }

    public ArrayList<String> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<String> weapons) {
        this.weapons = weapons;
    }
}
