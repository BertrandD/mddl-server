package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
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
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "items")
public class ItemInstance
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String templateId;

    @JsonView(View.Standard.class)
    private long count;

    @JsonView(View.Standard.class)
    private ItemType type;

    @DBRef
    @JsonManagedReference
    @JsonIgnore
    private Inventory inventory;

    public ItemInstance(){}

    public ItemInstance(String itemId, long count)
    {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
    }

    public ItemInstance(Inventory inventory, String itemId, long count)
    {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
        setInventory(inventory);
    }

    public Cargo getCargoItem(){
        if(isCargo()){
            return (Cargo) getTemplate();
        }
        return null;
    }

    public Engine getEngineItem(){
        if(isEngine()){
            return (Engine) getTemplate();
        }
        return null;
    }

    public Module getModuleItem(){
        if(isModule()){
            return (Module) getTemplate();
        }
        return null;
    }

    public Structure getStructureItem(){
        if(isStructure()){
            return (Structure) getTemplate();
        }
        return null;
    }

    public Weapon getWeaponItem(){
        if(isWeapon()){
            return (Weapon) getTemplate();
        }
        return null;
    }

    public GameItem getTemplate() {
        return ItemData.getInstance().getTemplate(getTemplateId());
    }

    public long getWeight(){
        return getTemplate().getWeight() * getCount();
    }

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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
