package com.middlewar.core.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.items.GameItem;
import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class FuncHolder {

    private String itemId;

    private String function;

    public FuncHolder(String itemId, String function){
        setItemId(itemId);
        setFunction(function);
    }

    @JsonIgnore
    public GameItem getItem(){
        return ItemData.getInstance().getResource(this.itemId);
    }
}
