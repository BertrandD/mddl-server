package com.middlewar.core.model;

import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.projections.BasePlanetScanProjection;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.PlanetScan;
import com.middlewar.core.utils.Observable;
import com.middlewar.core.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Getter
@Setter
@Entity
public class Player extends Observable {

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected PlayerInventory inventory;

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    private Account account;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Base> bases;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Base currentBase;

    @ManyToMany
    private List<Player> friends;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> emittedFriendRequests;

    @OneToMany(mappedBy = "requested", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> receivedFriendRequests;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Map<Long, PlanetScan> planetScans;

    private boolean deleted;

    private List<RecipeInstance> recipes;

    public Player() {
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setEmittedFriendRequests(new ArrayList<>());
        setReceivedFriendRequests(new ArrayList<>());
        setPlanetScans(new HashMap<>());
        setRecipes(new ArrayList<>());
    }

    public Player(Account account, String name) {
        setName(name);
        setAccount(account);
        setBases(new ArrayList<>());
        setFriends(new ArrayList<>());
        setEmittedFriendRequests(new ArrayList<>());
        setReceivedFriendRequests(new ArrayList<>());
        setPlanetScans(new HashMap<>());
        setInventory(new PlayerInventory(this));
        setRecipes(new ArrayList<>());
    }

    public void addBase(Base base) {
        getBases().add(base);
    }

    public boolean addFriend(Player friend) {
        return !getFriends().contains(friend) && getFriends().add(friend);
    }

    public boolean addRequest(FriendRequest request) {
        if (request.getRequester().is(this)) {
            return !getEmittedFriendRequests().contains(request) && getEmittedFriendRequests().add(request);
        } else
            return request.getRequested().is(this) && !getEmittedFriendRequests().contains(request) && getEmittedFriendRequests().add(request);
    }

    public void addPlanetScanned(Planet planet, Base base) {
        if (!planetScans.containsKey(planet.getId())) {
            planetScans.put(planet.getId(), new PlanetScan(planet));
        }
        final PlanetScan planetScan = planetScans.get(planet.getId());
        planetScan.getBaseScanned().put(base.getId(), new BasePlanetScanProjection(base));
        planetScan.setDate(TimeUtil.getCurrentTime());
    }

    public Map<Long, PlanetScan> getPlanetScans() {
        return planetScans;
    }

    public void setPlanetScans(Map<Long, PlanetScan> planetScans) {
        this.planetScans = planetScans;
    }

    public boolean is(Player player) {
        return player.getId() == this.getId();
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Player && ((Player) o).getId() == getId();
    }
}
