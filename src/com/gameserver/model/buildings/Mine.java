package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class Mine extends Building {

    private String produceItemId;
    private HashMap<Integer, Long> productionByLevel;

    public Mine(StatsSet set) {
        super(set);
        setProductionByLevel(new HashMap<>());
    }

    public String getProduceItemId() {
        return produceItemId;
    }

    public void setProduceItemId(String produceItemId) {
        this.produceItemId = produceItemId;
    }

    public HashMap<Integer, Long> getProductionByLevel() {
        return productionByLevel;
    }

    public void setProductionByLevel(HashMap<Integer, Long> productionByLevel) {
        this.productionByLevel = productionByLevel;
    }
}
