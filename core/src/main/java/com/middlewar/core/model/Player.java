package com.middlewar.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.PlanetScan;
import com.middlewar.core.serializer.PlayerSerializer;
import com.middlewar.core.utils.TimeUtil;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = PlayerSerializer.class)
public class Player {

    @Id
    @GeneratedValue
    private String id;

    private String name;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Base> bases;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Base currentBase;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    protected PlayerInventory inventory;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> friends;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FriendRequest> friendRequests;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, PlanetScan> planetScans;

    public Player() {
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setFriendRequests(new ArrayList<>());
        setReports(new ArrayList<>());
        setPlanetScans(new HashMap<>());
    }

    public Player(Account account, String name) {
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

    public boolean addFriend(Player friend) {
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
        planetScan.getBaseScanned().put(base.getId(), base);
        planetScan.setDate(TimeUtil.getCurrentTime());
    }

    public Map<String, PlanetScan> getPlanetScans() {
        return planetScans;
    }

    public void setPlanetScans(Map<String, PlanetScan> planetScans) {
        this.planetScans = planetScans;
    }

    public boolean is(Player player) {
        return player.getId().equalsIgnoreCase(this.getId());
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
