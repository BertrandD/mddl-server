package com.gameserver.model;

import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Requirement {

    private List<ItemHolder> items;
    private List<BuildingHolder> buildings;
    // TODO: Technologies

    public Requirement() {
        setItems(new ArrayList<>());
        setBuildings(new ArrayList<>());
    }

    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setItems(items);
        setBuildings(buildings);
    }

    public List<ItemHolder> getItems() {
        return items;
    }

    private void setItems(List<ItemHolder> items) {
        this.items = items;
    }

    public void addItem(ItemHolder item) {
        items.add(item);
    }

    public List<BuildingHolder> getBuildings() {
        return buildings;
    }

    private void setBuildings(List<BuildingHolder> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(BuildingHolder building) {
        buildings.add(building);
    }
}
