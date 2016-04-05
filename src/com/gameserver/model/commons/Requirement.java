package com.gameserver.model.commons;

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
    // private List<TechnologyHolder> technologies;

    public Requirement() {
        setItems(new ArrayList<>());
        setBuildings(new ArrayList<>());
        // setTechnologies(new ArrayList<>());
    }

    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setItems(items);
        setBuildings(buildings);
        // setTechnologies(new ArrayList<>());
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
