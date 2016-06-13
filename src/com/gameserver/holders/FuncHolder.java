package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.GameItem;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 */
public class FuncHolder {

    @JsonView(View.Standard.class)
    private String itemId;

    @JsonView(View.Standard.class)
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
