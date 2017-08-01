package com.middlewar.core.model.items;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.SystemMessageData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.interfaces.IStat;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.serializer.GameItemSerializer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@JsonSerialize(using = GameItemSerializer.class)
public abstract class GameItem implements IStat {

    private String itemId;
    private String nameId;
    private ItemType type;
    private String descriptionId;
    private long weight;
    private long volume;
    private Lang lang;
    private List<StatHolder> stats;

    public GameItem(StatsSet set) {
        setItemId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", ItemType.class, ItemType.NONE));
        setDescriptionId(set.getString("descriptionId"));
        setWeight(set.getLong("weight", 0));
        setVolume(set.getLong("volume", 0));
        setLang(Lang.EN);
        setStats(new ArrayList<>());
    }

    public String getName() {
        final String name = SystemMessageData.getInstance().getMessage(getLang(), getNameId());
        if (name == null) return "Unamed item [" + getItemId() + "]";
        return name;
    }

    public String getDescription() {
        final String descr = SystemMessageData.getInstance().getMessage(getLang(), getDescriptionId());
        if (descr == null) return "No description for item [" + getItemId() + "]";
        return descr;
    }

    @Deprecated
    public void handleEffect(final ObjectStat stats) {
        getAllStats().forEach(stat -> stats.add(stat.getStat(), stat.getValue(), stat.getOp()));
    }

    @Override
    public List<StatHolder> getAllStats() {
        return stats;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameItem) {
            final GameItem item = (GameItem) o;
            if (item.getItemId().equalsIgnoreCase(this.getItemId())) return true;
        }
        return false;
    }
}
