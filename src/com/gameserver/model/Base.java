package com.gameserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.ItemType;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.vehicles.Ship;
import com.util.data.json.View;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "bases")
public final class Base
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
    @JsonManagedReference
    private ResourceInventory resources;

    @DBRef
    @JsonIgnore
    @JsonManagedReference
    private BaseInventory commons;

    @DBRef
    @JsonIgnore
    @JsonManagedReference
    private BaseInventory shipItems;

    public Base() {
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
    }

    public Base(String name, Player owner) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
        setBuildingPositions(new HashMap<>());
    }

    @JsonView(View.Standard.class)
    @SuppressWarnings("unused")
    public HashMap<String, Long> getMaxVolumes() {
        final HashMap<String, Long> inventoriesVolumes = new HashMap<>();
        inventoriesVolumes.put("max_volume_resources", getResources().getMaxVolume());
        inventoriesVolumes.put("max_volume_items", getShipItems().getMaxVolume());
        return inventoriesVolumes;
    }

    @JsonView(View.Standard.class)
    public HashMap<String, List<ItemInstance>> getInventory() {
        final HashMap<String, List<ItemInstance>> inventory = new HashMap<>();
        inventory.put(ItemType.RESOURCE.toString(), getResources().getItems());
        inventory.put(ItemType.CARGO.toString(), getShipItems().getItems().stream().filter(ItemInstance::isCargo).collect(Collectors.toList()));
        inventory.put(ItemType.ENGINE.toString(), getShipItems().getItems().stream().filter(ItemInstance::isEngine).collect(Collectors.toList()));
        inventory.put(ItemType.MODULE.toString(), getShipItems().getItems().stream().filter(ItemInstance::isModule).collect(Collectors.toList()));
        inventory.put(ItemType.STRUCTURE.toString(), getShipItems().getItems().stream().filter(ItemInstance::isStructure).collect(Collectors.toList()));
        inventory.put(ItemType.WEAPON.toString(), getShipItems().getItems().stream().filter(ItemInstance::isWeapon).collect(Collectors.toList()));
        return inventory;
    }

    @SuppressWarnings("unused")
    @JsonView(View.Standard.class)
    public HashMap<String, Long> getProduction() {
        final HashMap<String, Long> production = new HashMap<>();

        // List of mines that produces item "metal"
        final List<BuildingInstance> mines = getBuildings().stream().filter(k ->
                k.getTemplate().getType().equals(BuildingCategory.Mine) &&
                ((Mine) k.getTemplate()).getItems().stream().filter(b ->
                        b.getItemId().equals("metal")).count() > 0
        ).collect(Collectors.toList());

        long total = 0;
        for (BuildingInstance mine : mines) {
            final Mine template = ((Mine) mine.getTemplate());
            total += template.getProduction(mine.getCurrentLevel());
        }

        production.put("metal", total);
        return production;
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

    @JsonIgnore
    public ResourceInventory getResources() {
        return resources;
    }

    public void setResources(ResourceInventory resources) {
        this.resources = resources;
    }

    @JsonIgnore
    public BaseInventory getCommons() {
        return commons;
    }

    public void setCommons(BaseInventory commons) {
        this.commons = commons;
    }

    @JsonIgnore
    public BaseInventory getShipItems() {
        return shipItems;
    }

    public void setShipItems(BaseInventory shipItems) {
        this.shipItems = shipItems;
    }
}
