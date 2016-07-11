package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.GameItem;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Extractor extends ModulableBuilding {

    private List<GameItem> produceItems;

    public Extractor(StatsSet set) {
        super(set);
    }

    public void setProduceItems(List<GameItem> produceItems) {
        this.produceItems = produceItems;
    }

    public List<GameItem> getProduceItems() {
        return produceItems;
    }
}
