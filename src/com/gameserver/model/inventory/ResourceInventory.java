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
@Document(collection = "resource_inventory")
public class ResourceInventory extends Inventory {

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Base base;

    @JsonView(View.Standard.class)
    private long lastRefresh;

    public ResourceInventory(){
        super();
    }

    public ResourceInventory(Base base){
        super();
        setBase(base);
        setLastRefresh(System.currentTimeMillis());
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
