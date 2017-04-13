package com.middlewar.core.model.buildings;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.BuildingCategory;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.interfaces.IStat;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.stats.BuildingStats;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.serializer.BuildingSerializer;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@JsonSerialize(using = BuildingSerializer.class)
public abstract class Building implements IStat {

    private String id;
    private String nameId;
    private String descriptionId;
    private BuildingCategory type;
    private int maxLevel;
    private int maxBuild;
    private long[] buildTimes;
    private long[] useEnergy;
    private BuildingStats stats;
    private HashMap<Integer, Requirement> requirements;
    private Lang lang = Lang.EN;

    public Building(StatsSet set) {
        setId(set.getString("id"));
        setNameId(set.getString("nameId"));
        setType(set.getEnum("type", BuildingCategory.class));
        setDescriptionId(set.getString("descriptionId"));
        setMaxLevel(set.getInt("maxLevel", 1));
        setMaxBuild(set.getInt("maxBuild", 1));

        setRequirements(new HashMap<>());
    }

    public long getUseEnergyAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel())
            return getUseEnergy()[level-1];
        else return 0;
    }

    public long getBuildTimeAtLevel(int level) {
        if(level > 0 && level <= getMaxLevel()) return getBuildTimes()[level-1];
        else return 0;
    }

    public void handleEffect(final ObjectStat stats, int level) {
        this.getStats().forEach(stat -> stats.add(stat.getStat(), stat.getValue(level), stat.getOp()));
    }

    @Override
    public List<StatHolder> getStats() {
        // Retrieve all stats for each level + global stats (with no level)
        return stats.getStats(getMaxLevel());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Building){
            final Building building = (Building) o;
            if(building.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
