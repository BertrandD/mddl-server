package com.gameserver.model;

import com.auth.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.model.inventory.PlayerInventory;
import com.serializer.PlayerSerializer;
import org.bson.types.ObjectId;
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
@JsonSerialize(using = PlayerSerializer.class)
public class Player {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @DBRef
    @Indexed
    private Account account;

    @DBRef
    @JsonBackReference
    private List<Base> bases;

    @DBRef
    @JsonBackReference
    private Base currentBase;

    @DBRef
    @JsonBackReference
    private PlayerInventory inventory;

    @DBRef
    private List<Player> friends;

    @DBRef
    private List<FriendRequest> friendRequests;

    public Player() {
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
    }

    public Player(Account account, String name)
    {
        setId(new ObjectId().toString());
        setName(name);
        setAccount(account);
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
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
        this.bases.add(base);
    }

    public Base getCurrentBase() {
        return currentBase;
    }

    public void setCurrentBase(Base currentBase) {
        this.currentBase = currentBase;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public List<Player> getFriends() {
        return friends;
    }

    public void setFriends(List<Player> friends) {
        this.friends = friends;
    }

    public boolean addFriend(Player friend) {
        return !getFriends().contains(friend) && getFriends().add(friend);
    }

    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public boolean addRequest(FriendRequest request) {
        return !getFriendRequests().contains(request) && getFriendRequests().add(request);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Player){
            final Player player = (Player) o;
            if(player.getId().equalsIgnoreCase(this.getId())) return true;
        }
        return false;
    }
}
