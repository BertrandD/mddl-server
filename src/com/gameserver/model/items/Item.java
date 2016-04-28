package com.gameserver.model.items;

import com.gameserver.enums.Rank;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.commons.StatsSet;

/**
 * @author LEBOC Philippe
 */
public abstract class Item extends GameItem
{
    private Rank rank;
    private long buildTime;

    public Item(StatsSet set, Requirement requirement)
    {
        super(set, requirement);
        setRank(set.getEnum("rank", Rank.class));
        setBuildTime(set.getLong("buildTime"));
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
}