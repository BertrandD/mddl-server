package com.middlewar.core.model.items;

import com.middlewar.core.enums.Rank;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class Item extends GameItem
{
    private Rank rank;
    private long buildTime;
    private Requirement requirement;

    public Item(StatsSet set, Requirement requirement)
    {
        super(set);
        setRank(set.getEnum("rank", Rank.class, Rank.NONE));
        setBuildTime(set.getLong("buildTime", 0L));

        setRequirement(requirement);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
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

    @Override
    public boolean equals(Object o){
        if(o instanceof Item){
            final Item item = (Item) o;
            if(item.getItemId().equalsIgnoreCase(this.getItemId())) return true;
        }
        return false;
    }
}