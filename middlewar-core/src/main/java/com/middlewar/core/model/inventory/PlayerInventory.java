package com.middlewar.core.model.inventory;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@NoArgsConstructor
public class PlayerInventory extends Inventory {

    @OneToOne
    private Player player;

    public PlayerInventory(Player player) {
        super();
        setPlayer(player);
    }

    @Override
    public ItemInstance getItem(String templateId) {
        return getItems()
                .stream()
                .filter(item -> item.getTemplateId().equals(templateId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public InventoryDTO toDTO() {
        return super.toDTO(new InventoryDTO());
    }

    @Override
    public long getAvailableCapacity() {
        return -1; // Unlimited
    }

    @Override
    public String toString() {
        return "PlayerInventory{" + super.toString() + "}";
    }
}
