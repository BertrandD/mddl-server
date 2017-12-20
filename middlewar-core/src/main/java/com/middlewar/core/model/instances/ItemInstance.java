package com.middlewar.core.model.instances;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.dto.inventory.ItemInstanceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
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
        return o != null && o instanceof ItemInstance && ((ItemInstance) o).getId() == getId();
    }
}
