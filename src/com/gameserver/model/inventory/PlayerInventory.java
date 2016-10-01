package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.model.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "player_inventory")
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
