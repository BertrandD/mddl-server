package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.model.Player;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "player_inventory")
public final class PlayerInventory extends AbstractMultiStorageInventory {

    @DBRef
    @JsonBackReference
    private Player player;

    public PlayerInventory() {
        super();
    }

    public PlayerInventory(Player player) {
        super();
        setPlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public long getMaxWeight() {
        return 9999999L;
    }

    @Override
    public long getWeight() {
        return 0;
    }

    @Override
    public long getFreeWeight() {
        return 0;
    }

    @Override
    public long getMaxVolume() {
        return 9999999L;
    }

    @Override
    public long getVolume() {
        return 0;
    }

    @Override
    public long getFreeVolume() {
        return 9999999L;
    }
}
