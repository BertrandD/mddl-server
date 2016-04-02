package com.gameserver.model.items;

import com.gameserver.enums.Rank;
import com.gameserver.model.Requirement;
import com.gameserver.model.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class Item
{
    private String itemId;
    private String name;
    private String description;
    private Rank rank;
    private int useSlot;
    private int weight;
    private boolean sellable;
    private boolean tradable;
    private boolean disabled;
    private long buildTime;
    private Requirement requirement;

    public Item(String id, String name, String description, Rank rank, int useSlot, boolean sellable, boolean tradable, boolean disabled, long buildTime, Requirement requirement)
    {
        setItemId(id);
        setName(name);
        setDescription(description);
        setRank(rank);
        setUseSlot(useSlot);
        setWeight(0); // TODO: fix
        setSellable(sellable);
        setTradable(tradable);
        setDisabled(disabled);
        setBuildTime(buildTime);
        setRequirement(requirement);
    }

    public Item(StatsSet set, Requirement requirement)
    {
        setItemId(set.getString("id"));
        setName(set.getString("name"));
        setDescription(set.getString("description"));
        setRank(set.getEnum("rank", Rank.class));
        setUseSlot(set.getInt("useslot", 0));
        setWeight(set.getInt("weight", 0));
        setSellable(set.getBoolean("sellable", false));
        setTradable(set.getBoolean("tradable", false));
        setDisabled(set.getBoolean("disabled", false));
        setBuildTime(set.getLong("buildtime", -1));

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public int getUseSlot() {
        return useSlot;
    }

    public void setUseSlot(int useSlot) {
        this.useSlot = useSlot;
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

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }
}