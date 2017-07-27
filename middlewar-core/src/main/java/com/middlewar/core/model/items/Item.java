package com.middlewar.core.model.items;

import com.middlewar.core.enums.Rank;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
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
        if (o instanceof Item) {
            final Item item = (Item) o;
            if (item.getItemId().equalsIgnoreCase(this.getItemId())) return true;
        }
        return false;
    }
}