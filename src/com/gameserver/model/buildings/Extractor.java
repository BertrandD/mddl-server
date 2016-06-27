package com.gameserver.model.buildings;

import com.gameserver.holders.FuncHolder;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.GameItem;
import com.util.Evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Extractor extends ModulableBuilding {

    private List<GameItem> produceItems;
    private HashMap<Integer, HashMap<String, Long>> production;

    public Extractor(StatsSet set) {
        super(set);
        initialize(set.getObject("production", List.class)); // TODO: unchecked here
    }

    private void initialize(List<FuncHolder> functions) {
        final List<GameItem> items = new ArrayList<>();
        final HashMap<Integer, HashMap<String, Long>> production = new HashMap<>();

        for(int i = 1; i <= getMaxLevel(); i++)
        {
            final HashMap<String, Long> tProduction = new HashMap<>();
            for (FuncHolder funcHolder : functions) {
                final long prod = ((Number)Evaluator.getInstance().eval(funcHolder.getFunction().replace("$level", ""+i))).longValue();
                tProduction.put(funcHolder.getItemId(), prod);
                items.add(funcHolder.getItem());
            }
            production.put(i, tProduction);
        }
        setProduceItems(items);
        setProduction(production);
    }

    public void setProduceItems(List<GameItem> produceItems) {
        this.produceItems = produceItems;
    }

    public List<GameItem> getProduceItems() {
        return produceItems;
    }

    public HashMap<String, Long> getProductionAtLevel(int level){
        return production.get(level);
    }

    public Long getProductionAtLevel(String itemId, int level) {
        return getProductionAtLevel(level).get(itemId);
    }

    public HashMap<Integer, HashMap<String, Long>> getProduction() {
        return production;
    }

    public void setProduction(HashMap<Integer, HashMap<String, Long>> production) {
        this.production = production;
    }
}
