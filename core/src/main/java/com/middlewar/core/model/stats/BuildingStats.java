package com.middlewar.core.model.stats;

import com.middlewar.core.holders.StatHolder;
import lombok.Data;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Leboc Philippe.
 */
@Data
@Embeddable
public class BuildingStats {

    private List<StatHolder> globalStats;
    private Map<Integer, List<StatHolder>> statsByLevel;

    public BuildingStats(){
        setGlobalStats(new ArrayList<>());
        setStatsByLevel(new HashMap<>());
    }

    /**
     * @param stat the stat to retrieve
     * @return the global stat corresponding to the given stat
     */
    public StatHolder getStat(Stats stat) {
        return retrieveStat(getGlobalStats(), stat);
    }

    /**
     * @param stat The Stat to retrieve
     * @param level The building level that has this Stat
     * @return The stat stored in StatsByLevel list corresponding to the given stat
     */
    public StatHolder getStat(Stats stat, int level) {
        if(!getStatsByLevel().containsKey(level)) return null;
        final List<StatHolder> stats = getStatsByLevel().get(level);
        return retrieveStat(stats, stat);
    }

    /**
     * @param level The building level
     * @return list of StatHolder that correspond to the given building level + global stats + statsByLevel < given level
     */
    public List<StatHolder> getStats(int level) {
        final List<StatHolder> stats = new ArrayList<>();
        stats.addAll(getGlobalStats());

        for(int i = 1; i <= level; i++){
            if(getStatsByLevel().containsKey(i))
                stats.addAll(getStatsByLevel().get(level));
        }

        return stats;
    }

    /**
     * @param stats The list of stats
     * @param statToFind The stats to find in "stats" param
     * @return The stat corresponding to the searched stat in the "stats" list
     */
    private StatHolder retrieveStat(List<StatHolder> stats, Stats statToFind) {
        return stats
                .stream()
                .filter(stat -> stat.getStat().ordinal() == statToFind.ordinal())
                .findFirst()
                .orElse(null);
    }
}
