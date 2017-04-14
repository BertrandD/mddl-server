package com.middlewar.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.PlanetScan;
import com.middlewar.core.holders.BaseHolder;
import com.middlewar.core.serializer.PlayerSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Data
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

    private Map<String, PlanetScan> planetScans;

    public Player() {
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setReports(new ArrayList<>());
        setPlanetScans(new HashMap<>());
    }

    public Player(Account account, String name) {
        setId(new ObjectId().toString());
        setName(name);
        setAccount(account);
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setReports(new ArrayList<>());
        setPlanetScans(new HashMap<>());
    }

    public void addBase(Base base) {
        getBases().add(base);
    }

    public boolean addFriend(PlayerHolder friend) {
        return !getFriends().contains(friend) && getFriends().add(friend);
    }

    public boolean addRequest(FriendRequest request) {
        return !getFriendRequests().contains(request) && getFriendRequests().add(request);
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public void addPlanetScanned(Planet planet, Base base) {
        if (!planetScans.containsKey(planet.getId())) {
            planetScans.put(planet.getId(), new PlanetScan(planet));
        }
        final PlanetScan planetScan = planetScans.get(planet.getId());
        planetScan.getBaseScanned().put(base.getId(), new BaseHolder(base));
        planetScan.setDate(System.currentTimeMillis());
    }

    public Map<String, PlanetScan> getPlanetScans() {
        return planetScans;
    }

    public void setPlanetScans(Map<String, PlanetScan> planetScans) {
        this.planetScans = planetScans;
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
