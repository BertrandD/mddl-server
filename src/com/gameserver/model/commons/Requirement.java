package com.gameserver.model.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Requirement {

    @JsonView(View.Standard.class)
    private List<FuncHolder> functions;

    @JsonView(View.Standard.class)
    private List<ItemHolder> items;

    @JsonView(View.Standard.class)
    private List<BuildingHolder> buildings;

    @JsonIgnore
    private int level;

    // TODO: Technologies
    // private List<TechnologyHolder> technologies;

    public Requirement() {
        setFunctions(new ArrayList<>());
        setItems(new ArrayList<>());
        setBuildings(new ArrayList<>());
        // setTechnologies(new ArrayList<>());
    }

    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setFunctions(new ArrayList<>());
        setItems(items);
        setBuildings(buildings);
        // setTechnologies(new ArrayList<>());
    }

    public Requirement(List<FuncHolder> functions, List<ItemHolder> items, List<BuildingHolder> buildings) {
        setFunctions(functions);
        setItems(items);
        setBuildings(buildings);
        // setTechnologies(new ArrayList<>());
    }

    @JsonView(View.Standard.class)
    public ArrayList<ItemHolder> getResources(){
        final ArrayList<ItemHolder> resourcesReq = new ArrayList<>();

        this.functions.forEach(k->{
            final String func = k.getFunction().replace("$level", ""+getLevel());
            final long count = ((Number) Evaluator.getInstance().eval(func)).longValue();
            resourcesReq.add(new ItemHolder(k.getId(), count));
        });

        return resourcesReq;
    }

    public List<FuncHolder> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FuncHolder> functions) {
        this.functions = functions;
    }

    public void addFunction(FuncHolder func){
        this.functions.add(func);
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

    @JsonIgnore
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
