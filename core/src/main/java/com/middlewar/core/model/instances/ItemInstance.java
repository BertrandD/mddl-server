package com.middlewar.core.model.instances;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.serializer.ItemInstanceSerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = ItemInstanceSerializer.class)
public class ItemInstance {

    @Id
    @GeneratedValue
    private String id;
    private String templateId;
    private double count;
    private ItemType type;

    @ManyToOne
    @JsonManagedReference
    private IInventory inventory;

    public ItemInstance(){}

    public ItemInstance(String itemId, double count)
    {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
    }

    public ItemInstance(IInventory inventory, String itemId, double count)
    {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
        setInventory(inventory);
    }

    public GameItem getTemplate() {
        return ItemData.getInstance().getTemplate(getTemplateId());
    }

    public long getWeight(){
        return (getTemplate().getWeight() * (long)Math.floor(getCount()));
    }

    public boolean isResource() { return getType().equals(ItemType.RESOURCE); }

    public boolean isCargo(){
        return getType() == ItemType.CARGO;
    }

    public boolean isEngine(){
        return getType() == ItemType.ENGINE;
    }

    public boolean isModule(){
        return getType() == ItemType.MODULE;
    }

    public boolean isWeapon(){
        return getType() == ItemType.WEAPON;
    }

    public boolean isStructure(){
        return getType() == ItemType.STRUCTURE;
    }

    public boolean isCommonItem(){
        return getType() == ItemType.RESOURCE;
    }

    public void addCount(double count) {
        setCount(getCount() + count);
    }

    public void removeCount(long count) {
        addCount(-count);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof ItemInstance){
            final ItemInstance item = (ItemInstance) o;
            if(item.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
