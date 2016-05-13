package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.commons.StatsSet;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class Storage extends Building {

    @JsonView(View.Standard.class)
    private String capacityFunc;

    private HashMap<Integer, Long> capacityBonusByLevel;

    public Storage(StatsSet set){
        super(set);
        setCapacityFunc(set.getString("capacity"));
        evaluateCapacity();
    }

    private void evaluateCapacity(){
        final HashMap<Integer, Long> capacityByLevel = new HashMap<>();
        for(int i = 1; i <= getMaxLevel(); i++)
        {
            final long capacity = ((Number) Evaluator.getInstance().eval(getCapacityFunc().replace("$level", ""+i))).longValue();
            capacityByLevel.put(i, capacity);
        }
        setCapacityBonus(capacityByLevel);
    }

    public Long getCapacityBonus(int level){
        return capacityBonusByLevel.get(level);
    }

    public HashMap<Integer, Long> getCapaictyBonus() {
        return capacityBonusByLevel;
    }

    public void setCapacityBonus(HashMap<Integer, Long> capacityBonusByLevel) {
        this.capacityBonusByLevel = capacityBonusByLevel;
    }

    public String getCapacityFunc() {
        return capacityFunc;
    }

    public void setCapacityFunc(String capacityFunc) {
        this.capacityFunc = capacityFunc;
    }
}
