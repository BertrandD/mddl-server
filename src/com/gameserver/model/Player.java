package com.gameserver.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.inventory.PlayerInventory;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "players")
public class Player {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String name;

    @DBRef
    @JsonView(View.Player_Bases.class)
    private List<Base> bases;

    @DBRef
    @JsonView(View.Player_Inventory.class)
    private PlayerInventory inventory;

    public Player(){
        bases = new ArrayList<>();
    }

    public Player(String name)
    {
        setId(null);
        setName(name);
        setBases(new ArrayList<>());
    }

    public Player(String id, String name, List<Base> bases){
        setId(id);
        setName(name);
        setBases(bases);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Base> getBases() {
        return bases;
    }

    public void setBases(List<Base> bases) {
        this.bases = bases;
    }

    public void addBase(Base base) {
        this.bases.add(base); // TODO: add check
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }
}
