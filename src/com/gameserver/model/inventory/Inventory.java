package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    private String id;

    @DBRef
    @JsonManagedReference
    private List<ItemInstance> items;


    protected Inventory() {
        setItems(new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    public void addItem(ItemInstance item) {
        if(getItems().stream().filter(k -> k.getTemplateId().equals(item.getTemplateId())).findFirst().orElse(null) == null)
            getItems().add(item);
        else System.out.println("TODO: not an error but must be rewrited.");
    }
}
