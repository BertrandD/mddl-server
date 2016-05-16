package com.gameserver.model.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Requirement {

    @JsonIgnore
    private int level;

    @JsonView(View.Standard.class)
    private List<FuncHolder> functions;

    @JsonView(View.Standard.class)
    private List<ItemHolder> items;

    @JsonView(View.Standard.class)
    private List<BuildingHolder> buildings;

    @JsonView(View.Standard.class)
    private HashMap<String, Long> resources;

    // TODO: Technologies
    // private List<TechnologyHolder> technologies;

    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(-1);
        setFunctions(null);
        setItems(items);
        setBuildings(buildings);
        // setTechnologies(new ArrayList<>());
        setResources(null);
    }

    public Requirement(int level, List<FuncHolder> functions, List<ItemHolder> items, List<BuildingHolder> buildings, HashMap<String, Long> resources) {
        setLevel(level);
        setFunctions(functions);
        setItems(items);
        setBuildings(buildings);
        // setTechnologies(new ArrayList<>());
        setResources(resources);

        evaluateResources();
    }

    public void evaluateResources() {
        this.functions.forEach(k -> {
            final String func = k.getFunction().replace("$level", ""+getLevel());
            final long count = ((Number) Evaluator.getInstance().eval(func)).longValue();
            getResources().put(k.getId(), count);
        });
    }

    public List<FuncHolder> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FuncHolder> functions) {
        this.functions = functions;
    }

    public List<ItemHolder> getItems() {
        return items;
    }

    private void setItems(List<ItemHolder> items) {
        this.items = items;
    }

    public List<BuildingHolder> getBuildings() {
        return buildings;
    }

    private void setBuildings(List<BuildingHolder> buildings) {
        this.buildings = buildings;
    }

    public HashMap<String, Long> getResources() {
        return resources;
    }

    public void setResources(HashMap<String, Long> resources) {
        this.resources = resources;
    }

    @JsonIgnore
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
