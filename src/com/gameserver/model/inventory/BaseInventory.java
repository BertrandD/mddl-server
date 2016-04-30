package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_inventory")
public class BaseInventory extends Inventory {

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Base base;

    public BaseInventory(){
        super();
    }

    public BaseInventory(Base base){
        super();
        setBase(base);
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }
}
