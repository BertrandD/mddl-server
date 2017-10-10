package com.middlewar.core.model.instances;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.serializer.ItemInstanceSerializer;
import com.middlewar.dto.inventory.ItemInstanceDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@NoArgsConstructor
@Entity
@JsonSerialize(using = ItemInstanceSerializer.class)
public class ItemInstance {

    @Id
    @GeneratedValue
    private long id;
    private String templateId;
    private double count;
    private ItemType type;

    @ManyToOne
    private Inventory inventory;

    public ItemInstance(String itemId, double count) {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
    }

    public ItemInstance(Inventory inventory, String itemId, double count) {
        setTemplateId(itemId);
        setType(getTemplate().getType());
        setCount(count);
        setInventory(inventory);
    }

    public ItemInstanceDTO toDTO() {
        ItemInstanceDTO dto = new ItemInstanceDTO();
        dto.setId(getId());
        dto.setCount(getCount());
        dto.setTemplateId(getTemplateId());
        dto.setType(getType().name());
        return dto;
    }

    public GameItem getTemplate() {
        return ItemData.getInstance().getTemplate(getTemplateId());
    }

    public long getWeight() {
        return (getTemplate().getWeight() * (long) Math.floor(getCount()));
    }

    public boolean isResource() {
        return getType().equals(ItemType.RESOURCE);
    }

    public boolean isCargo() {
        return getType() == ItemType.CARGO;
    }

    public boolean isEngine() {
        return getType() == ItemType.ENGINE;
    }

    public boolean isModule() {
        return getType() == ItemType.MODULE;
    }

    public boolean isWeapon() {
        return getType() == ItemType.WEAPON;
    }

    public boolean isStructure() {
        return getType() == ItemType.STRUCTURE;
    }

    public boolean isCommonItem() {
        return getType() == ItemType.RESOURCE;
    }

    public void addCount(double count) {
        setCount(getCount() + count);
    }

    public void removeCount(long count) {
        addCount(-count);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemInstance) {
            final ItemInstance item = (ItemInstance) o;
            if (item.getId() == this.getId()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemInstance{" +
                "id=" + id +
                ", templateId='" + templateId + '\'' +
                ", count=" + count +
                ", type=" + type +
                '}';
    }
}
