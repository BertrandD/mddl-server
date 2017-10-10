package com.middlewar.core.model.buildings;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.holders.PropertiesHolder;
import com.middlewar.core.holders.PropertyHolder;
import com.middlewar.core.holders.PropertyListHolder;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.items.Item;
import com.middlewar.dto.BuildingDTO;
import com.middlewar.dto.buildings.ItemFactoryDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public abstract class ItemFactory<T extends Item> extends Building {

    private HashMap<Integer, List<T>> itemsByLevel;

    public ItemFactory(StatsSet set) {
        super(set);
        initialize(set.getObject("propertiesByLevel", PropertiesHolder.class));
    }

    @Override
    public BuildingDTO toDTO() {
        ItemFactoryDTO dto = super.toDTO(new ItemFactoryDTO());
        dto.setItemsByLevel(new HashMap<>());
        itemsByLevel.forEach((k,v) -> dto.getItemsByLevel().put(k, v.stream().map(Item::getItemId).collect(Collectors.toList())));
        return dto;
    }

    private void initialize(final PropertiesHolder properties) {
        setItemsByLevel(new HashMap<>());

        if (properties == null || properties.getPropertiesByLevel() == null) {
            log.warn("PropertyByLevel is null !");
            return;
        }

        for (Map.Entry<Integer, List<PropertyListHolder>> entry : properties.getPropertiesByLevel().entrySet()) {
            addItemLevel(entry.getKey(), entry.getValue());
        }
    }

    public boolean hasItem(int level, String itemId) {
        final List<T> items = getItemsByLevel(level);
        return items != null && items.stream().filter(k -> k.getItemId().equals(itemId)).count() > 0;
    }

    public List<T> getItemsByLevel(int level) {
        final List<T> all = new ArrayList<>();
        for (int i = 1; i <= level; i++)
            if (getItemsByLevel().containsKey(i))
                all.addAll(getItemsByLevel().get(i));
        return all;
    }

    public HashMap<Integer, List<T>> getItemsByLevel() {
        return itemsByLevel;
    }

    public void setItemsByLevel(HashMap<Integer, List<T>> itemsByLevel) {
        this.itemsByLevel = itemsByLevel;
    }

    public void addItemLevel(int level, List<PropertyListHolder> list) {
        final List<T> items = new ArrayList<>();
        for (PropertyListHolder propertyListHolder : list) {
            for (PropertyHolder propertyHolder : propertyListHolder.getProperties()) {
                T item = null;
                switch (propertyHolder.getName()) {
                    case "module":
                        item = (T) ItemData.getInstance().getModule(propertyHolder.getValue());
                        break;
                    case "structure":
                        item = (T) ItemData.getInstance().getStructure(propertyHolder.getValue());
                        break;
                    default:
                        log.warn("Unknown propertyHolder name '" + propertyHolder.getName() + "' !");
                        break;
                }
                if (item != null) {
                    items.add(item);
                }
            }
        }
        getItemsByLevel().put(level, items);
    }
}
