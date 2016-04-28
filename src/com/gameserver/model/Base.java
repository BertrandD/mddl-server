package com.gameserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.ItemType;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.vehicles.Ship;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "bases")
public final class Base implements IInventory
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String name;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private Player owner;

    @JsonView(View.Standard.class)
    private HashMap<Integer, String> buildingPositions;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonView(View.Standard.class)
    private List<Ship> ships;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> metal;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> crystal;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> cargos;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> engines;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> modules;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> structures;

    @DBRef
    @JsonIgnore
    @JsonView(View.Standard.class)
    private List<ItemInstance> weapons;

    public Base(){
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
        setBuildingPositions(new HashMap<>());

        // Inventory
        setMetal(new ArrayList<>());
        setCrystal(new ArrayList<>());
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setStructures(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    public Base(String name, Player owner)
    {
        setId(null);
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
        setBuildingPositions(new HashMap<>());

        // Inventory
        setMetal(new ArrayList<>());
        setCrystal(new ArrayList<>());
        setCargos(new ArrayList<>());
        setEngines(new ArrayList<>());
        setModules(new ArrayList<>());
        setStructures(new ArrayList<>());
        setWeapons(new ArrayList<>());
    }

    @JsonView(View.Standard.class)
    public HashMap<String, List<ItemInstance>> getInventory(){
        final HashMap<String, List<ItemInstance>> inventory = new HashMap<>();
        final List<ItemInstance> resources = new ArrayList<>(metal);
        resources.addAll(getMetal());
        resources.addAll(getCrystal());
        inventory.put(ItemType.RESOURCE.toString(), resources);
        inventory.put(ItemType.CARGO.toString(), getCargos());
        inventory.put(ItemType.ENGINE.toString(), getEngines());
        inventory.put(ItemType.MODULE.toString(), getModules());
        inventory.put(ItemType.STRUCTURE.toString(), getStructures());
        inventory.put(ItemType.WEAPON.toString(), getWeapons());
        return inventory;
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

    public HashMap<Integer, String> getBuildingPositions() {
        return buildingPositions;
    }

    public void setBuildingPositions(HashMap<Integer, String> buildingPositions) {
        this.buildingPositions = buildingPositions;
    }

    @JsonView(View.Standard.class)
    public List<BuildingInstance> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingInstance> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(BuildingInstance building, int position) {
        this.buildings.add(building);
        this.buildingPositions.put(position, building.getId());
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    // INVENTORIES

    public List<ItemInstance> getMetal() {
        return metal;
    }

    public void setMetal(List<ItemInstance> metal) {
        this.metal = metal;
    }

    public List<ItemInstance> getCrystal() {
        return crystal;
    }

    public void setCrystal(List<ItemInstance> crystal) {
        this.crystal = crystal;
    }

    public List<ItemInstance> getCargos() {
        return cargos;
    }

    public void setCargos(List<ItemInstance> cargos) {
        this.cargos = cargos;
    }

    public List<ItemInstance> getEngines() {
        return engines;
    }

    public void setEngines(List<ItemInstance> engines) {
        this.engines = engines;
    }

    public List<ItemInstance> getModules() {
        return modules;
    }

    public void setModules(List<ItemInstance> modules) {
        this.modules = modules;
    }

    public List<ItemInstance> getStructures() {
        return structures;
    }

    public void setStructures(List<ItemInstance> structures) {
        this.structures = structures;
    }

    public List<ItemInstance> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<ItemInstance> weapons) {
        this.weapons = weapons;
    }

    @Override
    public boolean isAllowedToStore(ItemInstance item) {
        return false;
    }

    @Override
    public long getMaxCapacity() {
        return 0;
    }

    @Override
    public long getFreeCapacity() {
        return 0;
    }

    @Override
    public long getCurrentCapacityCharge() {
        return 0;
    }

    @Override
    public boolean addItem(ItemInstance item) {
        return false;
    }

    @Override
    public ItemInstance consumeItem(ItemInstance item) {
        return null;
    }

    @Override
    public ItemInstance consumeItem(String id, long count) {
        return null;
    }
}
