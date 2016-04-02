package com.gameserver.model.items;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Attachment;
import com.gameserver.model.Base;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "base_items")
public final class ItemInstance
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonView(View.ItemInstance_Base.class)
    private Base owner;

    @JsonView(View.Standard.class)
    private String itemId;

    @JsonView(View.Standard.class)
    private long count;

    @Transient
    private Item template;

    @DBRef
    @JsonView(View.Standard.class)
    private Attachment attachments;

    public ItemInstance(){}

    public ItemInstance(Base owner, String itemId, long count, Item template)
    {
        setId(null);
        setOwner(owner);
        setItemId(itemId);
        setCount(count);
        setTemplate(template);
    }

    public ItemInstance(String id, Base owner, String itemId, long count)
    {
        setId(id);
        setOwner(owner);
        setItemId(itemId);
        setCount(count);
        setTemplate(ItemData.getInstance().getTemplate(itemId));
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

    public void addCargo(ItemInstance cargo){
        if(isStructure()){
            getAttachments().getCargos().add(cargo);
        }
    }

    public void addEngine(ItemInstance engine){
        if(isStructure()){
            getAttachments().getEngines().add(engine);
        }
    }

    // TODO: addModule & addWeapon & addTechnology

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Base getOwner() {
        return owner;
    }

    public void setOwner(Base owner) {
        this.owner = owner;
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

    public Item getTemplate() {
        return template;
    }

    private void setTemplate(Item template) {
        this.template = template;
    }

    public Attachment getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachment attachments) {
        this.attachments = attachments;
    }
}
