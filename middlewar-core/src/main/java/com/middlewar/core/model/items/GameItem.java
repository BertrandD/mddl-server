package com.middlewar.core.model.items;

import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.interfaces.IStat;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public abstract class GameItem implements IStat {

    private String itemId;
    private String nameId;
    private ItemType type;
    private String descriptionId;
    private long weight;
    private long volume;
    private Lang lang;
    private Map<Stats, StatHolder> stats;

    public GameItem(StatsSet set) {
        setItemId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", ItemType.class, ItemType.NONE));
        setDescriptionId(set.getString("descriptionId"));
        setWeight(set.getLong("weight", 0));
        setVolume(set.getLong("volume", 0));
        setLang(Lang.EN);
        setStats(new HashMap<>());
    }

    public void addStats(List<StatHolder> statHolders) {
        statHolders.forEach(k->stats.put(k.getStat(), k));
    }

    @Deprecated
    public void handleEffect(final ObjectStat stats) {
        //getAllStats().forEach(stat -> stats.add(stat.getStat(), stat.getValue(), stat.getOp()));
    }

    @Override
    public List<StatHolder> getAllStats() {
        return new ArrayList<>(stats.values());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof GameItem && ((GameItem) o).itemId.equalsIgnoreCase(itemId);
    }
}
