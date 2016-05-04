package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "player_inventory")
public class PlayerInventory extends Inventory {

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Player player;

    public PlayerInventory(){
        super();
    }

    public PlayerInventory(Player player){
        super();
        setPlayer(player);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
