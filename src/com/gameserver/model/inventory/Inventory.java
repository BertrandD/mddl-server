package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private List<ItemInstance> items;

    public Inventory(){
        setItems(new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    @Override
    public long getMaxWeight() {
        return 0;
    }

    @Override
    public long getWeight() {
        return 0;
    }

    @Override
    public long getFreeWeight() {
        return 0;
    }

    @Override
    public long getMaxVolume() {
        return 0;
    }

    @Override
    public long getVolume() {
        return 0;
    }

    @Override
    public long getFreeVolume() {
        return 0;
    }

    @Override
    /**
     * WARNING: logic not completed. Don't use this method.
     * TODO: put me to database
     */
    public boolean addItem(String id, long count) {
        final ItemInstance item = getItems().stream().filter(k->k.getItemId().equals(id)).findFirst().orElse(null);
        if(item == null){
            getItems().add(new ItemInstance(this, id, count));
        }else{
            item.setCount(item.getCount()+count);
        }
        return true;
    }

    @Override
    public boolean addItem(ItemInstance item) {
        // TODO: check inventory capacity
        if(!getItems().contains(item)) {
            getItems().add(item);
        }else{
            ItemInstance inst = getItems().stream().filter(k->k.getItemId().equals(item.getItemId())).findFirst().orElse(null);
            inst.setCount(inst.getCount()+item.getCount());
        }
        return true;
    }

    @Override
    public boolean consumeItem(ItemInstance item, long count) {
        final ItemInstance inst = getItems().stream().filter(k->k.equals(item)).findFirst().orElse(null);
        if(inst != null){
            long result = inst.getCount() - count;
            if(result > 0) {
                inst.setCount(result);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean consumeItem(String id, long count) {
        return false;
    }
}
