package com.gameserver.model.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.FuncHolder;
import com.gameserver.holders.ItemHolder;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Requirement {

    @JsonView(View.Standard.class)
    private int level;

    @JsonView(View.Standard.class)
    private List<ItemHolder> items;

    @JsonIgnore
    private List<FuncHolder> functions;

    @JsonView(View.Standard.class)
    private List<BuildingHolder> buildings;

    /**
     * Used to parse ItemData
     * @param items
     * @param buildings
     */
    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(-1);
        setItems(items);
        setBuildings(buildings);
    }

    /**
     * Used to parse BuildingData
     * @param level
     * @param items
     * @param functions
     * @param buildings
     */
    public Requirement(int level, List<ItemHolder> items, List<FuncHolder> functions, List<BuildingHolder> buildings) {
        setLevel(level);
        setItems(items);
        setFunctions(functions);
        setBuildings(buildings);
    }

    private void initialize() {
        this.functions.forEach(k ->
        {
            String func = k.getFunction();
            if(func != null) {
                func = func.replace("$level", "" + getLevel());
                final long count = ((Number) Evaluator.getInstance().eval(func)).longValue();

                final ItemHolder currentItem = getItems().stream().filter(h -> h.getId().equals(k.getItemId())).findFirst().orElse(null);
                if (currentItem == null) getItems().add(new ItemHolder(k.getItemId(), count));
                else currentItem.setCount(currentItem.getCount() + count);
            }
        });
    }

    public List<ItemHolder> getItems() {
        return items;
    }

    private void setItems(List<ItemHolder> items) {
        this.items = items;
    }

    public List<FuncHolder> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FuncHolder> functions) {
        this.functions = functions;
        initialize();
    }

    public List<BuildingHolder> getBuildings() {
        return buildings;
    }

    private void setBuildings(List<BuildingHolder> buildings) {
        this.buildings = buildings;
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }
}
