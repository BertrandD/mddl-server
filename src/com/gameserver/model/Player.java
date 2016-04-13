package com.gameserver.model;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.inventory.PlayerInventory;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(unique = true)
    private String name;

    @DBRef
    @JsonView(View.Standard.class)
    private Account account;

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private List<Base> bases;

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private PlayerInventory inventory;

    public Player(){
        bases = new ArrayList<>();
    }

    public Player(Account account, String name)
    {
        setName(name);
        setAccount(account);
        setBases(new ArrayList<>());
    }

    public Player(String name)
    {
        setName(name);
        setBases(new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
