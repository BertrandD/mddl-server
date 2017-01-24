package com.middlewar.core.model.instances;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.serializer.ItemInstanceSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "items")
@JsonSerialize(using = ItemInstanceSerializer.class)
public class ItemInstance
{
    @Id
    private String id;
    private String templateId;
    private double count;
    private ItemType type;

    @DBRef
    @JsonManagedReference
    private IInventory inventory;

    public ItemInstance(){}

    public ItemInstance(String itemId, double count)
    {
        setId(new ObjectId().toString());
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
    }

    public ItemInstance(IInventory inventory, String itemId, double count)
    {
        setId(new ObjectId().toString());
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public void addCount(double count) {
        setCount(getCount() + count);
    }

    public void removeCount(long count) {
        addCount(-count);
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) { this.count = count; }

    public IInventory getInventory() {
        return inventory;
    }

    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
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