package com.gameserver.model.items;

import com.gameserver.enums.ItemType;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class GameItem {

    private String itemId;
    private String name;
    private ItemType type;
    private String descriptionId;
    private long weight;
    private long volume;
    private Requirement requirement;

    public GameItem(StatsSet set, Requirement requirement){
        setItemId(set.getString("id"));
        setName(set.getString("name"));
        setType(set.getEnum("type", ItemType.class, ItemType.NONE));
        setDescriptionId(set.getString("descriptionId"));
        setWeight(set.getLong("weight"));
        setVolume(set.getLong("volume"));

        setRequirement(requirement);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }
}
