package com.gameserver.model.items;

import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class GameItem {

    private String itemId;
    private String name;
    private int descriptionId;
    private int weight;
    private boolean sellable;
    private boolean tradable;
    private boolean disabled;
    private Requirement requirement;

    public GameItem(StatsSet set, Requirement requirement){
        setItemId(set.getString("id"));
        setName(set.getString("name"));
        setDescriptionId(set.getInt("descriptionId"));
        setWeight(set.getInt("weight"));
        setSellable(set.getBoolean("sellable", false));
        setTradable(set.getBoolean("tradable", false));
        setDisabled(set.getBoolean("disabled"));

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

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSellable() {
        return sellable;
    }

    public void setSellable(boolean sellable) {
        this.sellable = sellable;
    }

    public boolean isTradable() {
        return tradable;
    }

    public void setTradable(boolean tradable) {
        this.tradable = tradable;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }
}
