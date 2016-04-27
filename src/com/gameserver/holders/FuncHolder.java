package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.GameItem;
import com.util.data.json.View;

/**
 * @author LEBOC Philippe
 * Store object for calculating resources requirements
 */
public class FuncHolder {

    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String function;

    public FuncHolder(String id, String function){
        setId(id);
        setFunction(function);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @JsonIgnore
    public GameItem getItem(){
        return ItemData.getInstance().getResource(this.id);
    }
}
