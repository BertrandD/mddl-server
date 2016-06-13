package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.holders.FuncHolder;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.GameItem;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
public class Extractor extends ModulableBuilding {

    @JsonIgnore
    private List<FuncHolder> productionsFunctions;

    @JsonView(View.Standard.class)
    private HashMap<Integer, HashMap<String, Long>> production;

    @SuppressWarnings("unchecked")
    public Extractor(StatsSet set) {
        super(set);
        setProductionsFunctions(set.getObject("production", List.class));
    }

    private void evaluateProduction() {
        final HashMap<Integer, HashMap<String, Long>> production = new HashMap<>();
        for(int i = 1; i <= getMaxLevel(); i++)
        {
            final HashMap<String, Long> tProduction = new HashMap<>();
            for (FuncHolder funcHolder : getProductionsFunctions()) {
                final long prod = ((Number)Evaluator.getInstance().eval(funcHolder.getFunction().replace("$level", ""+i))).longValue();
                tProduction.put(funcHolder.getItemId(), prod);
            }
            production.put(i, tProduction);
        }

        setProduction(production);
    }

    public List<GameItem> getProduceItems() {
        return getProductionsFunctions().stream().map(FuncHolder::getItem).collect(Collectors.toList());
    }

    public HashMap<String, Long> getProductionAtLevel(int level){
        return production.get(level);
    }

    public Long getProductionAtLevel(String itemId, int level) {
        return getProductionAtLevel(level).get(itemId);
    }

    public List<FuncHolder> getProductionsFunctions() {
        return productionsFunctions;
    }

    public void setProductionsFunctions(List<FuncHolder> productionsFunctions) {
        this.productionsFunctions = productionsFunctions;
        evaluateProduction();
    }

    public HashMap<Integer, HashMap<String, Long>> getProduction() {
        return production;
    }

    public void setProduction(HashMap<Integer, HashMap<String, Long>> production) {
        this.production = production;
    }
}
