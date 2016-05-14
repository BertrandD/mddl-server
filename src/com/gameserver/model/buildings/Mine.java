package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.GameItem;
import com.util.Evaluator;
import com.util.data.json.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Mine extends Building {

    @JsonView(View.Standard.class)
    private List<GameItem> items;

    @JsonView(View.Standard.class)
    private String function;

    @JsonView(View.Standard.class)
    private HashMap<Integer, Long> production;

    public Mine(StatsSet set) {
        super(set);
        setFunction(set.getString("produce_function"));
        final String[] itemsIds = set.getString("produce").split(";");

        final List<GameItem> items = new ArrayList<>();
        for (String id : itemsIds) {
            final GameItem item = ItemData.getInstance().getTemplate(id);
            if(item != null) items.add(item);
        }

        setItems(items);

        evaluateProduction();
    }

    private void evaluateProduction(){
        final HashMap<Integer, Long> production = new HashMap<>();
        for(int i = 1; i <= getMaxLevel(); i++)
        {
            final long prod = ((Number)Evaluator.getInstance().eval(getFunction().replace("$level", ""+i))).longValue();
            production.put(i, prod);
        }
        setProduction(production);
    }

    public List<GameItem> getItems() {
        return items;
    }

    public void setItems(List<GameItem> items) {
        this.items = items;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public long getProduction(int level){
        return production.get(level);
    }

    public HashMap<Integer, Long> getProduction() {
        return production;
    }

    public void setProduction(HashMap<Integer, Long> production) {
        this.production = production;
    }
}
