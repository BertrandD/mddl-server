package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.serializer.ItemInstanceSerializer;
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
    private Inventory inventory;

    public ItemInstance(){}

    public ItemInstance(String itemId, double count)
    {
        setId(new ObjectId().toString());
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
    }

    public ItemInstance(Inventory inventory, String itemId, double count)
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

    public boolean isResource() { return isCommonItem() && getType().equals(ItemType.RESOURCE); }

    public boolean isCargo(){
        return getTemplate() instanceof Cargo;
    }

    public boolean isEngine(){
        return getTemplate() instanceof Engine;
    }

    public boolean isModule(){
        return getTemplate() instanceof Module;
    }

    public boolean isWeapon(){
        return getTemplate() instanceof Weapon;
    }

    public boolean isStructure(){
        return getTemplate() instanceof Structure;
    }

    public boolean isCommonItem(){
        return getTemplate() instanceof CommonItem;
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

    public double getCount() {
        return count;
    }

    public void setCount(double count) { this.count = count; }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
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
