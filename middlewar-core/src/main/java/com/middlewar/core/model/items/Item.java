package com.middlewar.core.model.items;

import com.middlewar.core.enums.Rank;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public abstract class Item extends GameItem {

    private Rank rank;
    private long buildTime;
    private Requirement requirement;

    public Item(StatsSet set, Requirement requirement) {
        super(set);
        setRank(set.getEnum("rank", Rank.class, Rank.NONE));
        setBuildTime(set.getLong("buildTime", 0L));
        setRequirement(requirement);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Item && ((Item) o).getItemId().equalsIgnoreCase(getItemId());
    }
}