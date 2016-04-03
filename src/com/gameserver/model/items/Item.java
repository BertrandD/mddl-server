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
    private int useSlot; // TODO: move it
    private long buildTime;

    public Item(StatsSet set, Requirement requirement)
    {
        super(set, requirement);
        setRank(set.getEnum("rank", Rank.class));
        setUseSlot(set.getInt("useslot", 0));
        setBuildTime(set.getLong("buildtime", -1));
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

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }
}