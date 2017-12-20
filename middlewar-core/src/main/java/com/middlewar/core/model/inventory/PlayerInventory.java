package com.middlewar.core.model.inventory;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
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
    public long getAvailableCapacity() {
        return -1; // Unlimited
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof PlayerInventory && ((PlayerInventory) o).getId() == getId();
    }
}
