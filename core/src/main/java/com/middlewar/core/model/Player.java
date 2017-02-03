package com.middlewar.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.serializer.PlayerSerializer;
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

    private List<PlayerHolder> friends;

    @DBRef
    private List<FriendRequest> friendRequests;

    @DBRef
    private List<Report> reports;

    public Player() {
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setReports(new ArrayList<>());
    }

    public Player(Account account, String name) {
        setId(new ObjectId().toString());
        setName(name);
        setAccount(account);
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setReports(new ArrayList<>());
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

    public List<PlayerHolder> getFriends() {
        return friends;
    }

    public void setFriends(List<PlayerHolder> friends) {
        this.friends = friends;
    }

    public boolean addFriend(PlayerHolder friend) {
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

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
