package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.Base;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class BaseInventory extends Inventory {

    @DBRef
    @JsonIgnore
    private Base base;

    @JsonView(View.Standard.class)
    private List<ItemHolder> commons;

    @JsonView(View.Standard.class)
    private List<ItemHolder> cargos;

    @JsonView(View.Standard.class)
    private List<ItemHolder> engines;

    @JsonView(View.Standard.class)
    private List<ItemHolder> modules;

    @JsonView(View.Standard.class)
    private List<ItemHolder> structures;

    @JsonView(View.Standard.class)
    private List<ItemHolder> weapons;

    public BaseInventory(){
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setStructures(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    public BaseInventory(Base base){
        setBase(base);
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setStructures(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    @Override
    public long getMaxWeight() {
        return 0; // TODO
    }

    @JsonIgnore
    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public List<ItemHolder> getCommons() {
        return commons;
    }

    public void setCommons(List<ItemHolder> commons) {
        this.commons = commons;
    }

    public List<ItemHolder> getCargos() {
        return cargos;
    }

    public void setCargos(List<ItemHolder> cargos) {
        this.cargos = cargos;
    }

    public List<ItemHolder> getEngines() {
        return engines;
    }

    public void setEngines(List<ItemHolder> engines) {
        this.engines = engines;
    }

    public List<ItemHolder> getModules() {
        return modules;
    }

    public void setModules(List<ItemHolder> modules) {
        this.modules = modules;
    }

    public List<ItemHolder> getStructures() {
        return structures;
    }

    public void setStructures(List<ItemHolder> structures) {
        this.structures = structures;
    }

    public List<ItemHolder> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<ItemHolder> weapons) {
        this.weapons = weapons;
    }
}
