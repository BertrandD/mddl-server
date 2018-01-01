package com.middlewar.core.model.instances;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.items.GameItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    @NotEmpty
    private String templateId;

    @Min(0)
    private double count;

    @NotNull
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
