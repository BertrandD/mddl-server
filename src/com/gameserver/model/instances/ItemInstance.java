package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.CommonItem;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "items")
public final class ItemInstance
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String itemId;

    @JsonView(View.Standard.class)
    private long count;

    @Transient
    private GameItem template;

    public ItemInstance(){}

    public ItemInstance(String itemId, long count, GameItem template)
    {
        setId(null);
        setItemId(itemId);
        setCount(count);
        setTemplate(template);
    }

    public ItemInstance(String id, String itemId, long count)
    {
        setId(id);
        setItemId(itemId);
        setCount(count);
        setTemplate(ItemData.getInstance().getTemplate(itemId));
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

    // TODO: addModule & addWeapon & addTechnology

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public GameItem getTemplate() {
        return template;
    }

    private void setTemplate(GameItem template) {
        this.template = template;
    }
}
