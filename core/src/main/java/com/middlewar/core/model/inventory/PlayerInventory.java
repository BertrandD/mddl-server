package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.serializer.PlayerInventorySerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = PlayerInventorySerializer.class)
public final class PlayerInventory extends Inventory {

    @OneToOne
    @JsonBackReference
    private Player player;

    public PlayerInventory() {
        super();
    }

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
}
