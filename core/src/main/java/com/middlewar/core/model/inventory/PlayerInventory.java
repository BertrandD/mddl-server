package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.serializer.PlayerInventorySerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Data
@Document(collection = "player_inventory")
@JsonSerialize(using = PlayerInventorySerializer.class)
public final class PlayerInventory extends Inventory {

    @DBRef
    @JsonBackReference
    private Player player;

    public PlayerInventory() {
        super();
    }

    public PlayerInventory(Player player) {
        super();
        setId(new ObjectId().toString());
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
