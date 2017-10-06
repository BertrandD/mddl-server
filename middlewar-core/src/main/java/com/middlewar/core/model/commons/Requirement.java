package com.middlewar.core.model.commons;

import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.ItemHolder;
import com.middlewar.dto.commons.RequirementDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Data
public class Requirement {

    private int level;
    private List<ItemHolder> items;
    private List<BuildingHolder> buildings;

    /**
     * Used to parse ItemData
     *
     * @param items     list of ItemHolder of the requirement
     * @param buildings list of BuildingHolder of the requirement
     */
    public Requirement(List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(-1);
        setItems(items);
        setBuildings(buildings);
    }

    /**
     * Used to parse BuildingData
     *
     * @param level     The level of the requirement
     * @param items     list of ItemHolder of the requirement
     * @param buildings list of BuildingHolder of the requirement
     */
    public Requirement(int level, List<ItemHolder> items, List<BuildingHolder> buildings) {
        setLevel(level);
        setItems(items);
        setBuildings(buildings);
    }

    /**
     * Used to parse BuildingData (completion with functions)
     *
     * @param level The level of the requirement
     * @param item  The item to add to the requirement
     */
    public Requirement(int level, ItemHolder item) {
        setLevel(level);
        setItems(new ArrayList<>());
        setBuildings(new ArrayList<>());

        addItem(item);
    }

    public RequirementDTO toDTO() {
        RequirementDTO dto = new RequirementDTO();
        dto.setLevel(getLevel());
        dto.setBuildings(getBuildings().stream().map(BuildingHolder::toDTO).collect(Collectors.toList()));
        dto.setItems(getItems().stream().map(ItemHolder::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public void addItem(ItemHolder item) {
        final ItemHolder alreadyExistingItem =
                getItems()
                        .stream()
                        .filter(holder -> holder.getId().equals(item.getId()))
                        .findFirst()
                        .orElse(null);

        if (alreadyExistingItem != null)
            alreadyExistingItem.setCount(alreadyExistingItem.getCount() + item.getCount());
        else
            getItems().add(item);
    }
}
