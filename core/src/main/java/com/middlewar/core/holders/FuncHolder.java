package com.middlewar.core.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.items.GameItem;

/**
 * @author LEBOC Philippe
 */
public class FuncHolder {

    private String itemId;

    private String function;

    public FuncHolder(String itemId, String function){
        setItemId(itemId);
        setFunction(function);
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @JsonIgnore
    public GameItem getItem(){
        return ItemData.getInstance().getResource(this.itemId);
    }
}