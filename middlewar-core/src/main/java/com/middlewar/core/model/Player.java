package com.middlewar.core.model;

import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.projections.BasePlanetScanProjection;
import com.middlewar.core.model.social.FriendRequest;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.PlanetScan;
import com.middlewar.core.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @ManyToOne
    private Account account;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Base> bases;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Base currentBase;

    @ManyToMany
    private List<Player> friends;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected PlayerInventory inventory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> friendRequests;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PlanetScan> planetScans;

    @OneToMany
    private List<RecipeInstance> recipes;

    public Player() {
        setBases(emptyList());
        setFriends(emptyList());
        setFriendRequests(emptyList());
        setPlanetScans(emptyList());
        setRecipes(emptyList());
    }

    public Player(Account account, String name) {
        setName(name);
        setAccount(account);
        setBases(emptyList());
        setFriends(emptyList());
        setFriendRequests(emptyList());
        setPlanetScans(emptyList());
        setInventory(new PlayerInventory(this));
        setRecipes(emptyList());
    }

    public void addBase(@NotNull Base base) {
        getBases().add(base);
    }

    public boolean addFriend(@NotNull Player friend) {
        return friends != null && !getFriends().contains(friend) && getFriends().add(friend);
    }

    public boolean addRequest(@NotNull FriendRequest request) {
        if(friendRequests != null && !friendRequests.contains(request)) {
            friendRequests.add(request);
            return true;
        }

        log.warn("Cannot add FriendRequest " + request.getId() + " because of null or already exist");
        return false;
    }

    public void addPlanetScanned(Planet planet, Base base) {
        final PlanetScan planetScan = new PlanetScan(planet);
        planetScan.getBaseScanned().put(base.getId(), new BasePlanetScanProjection(base));
        planetScan.setDate(TimeUtil.getCurrentTime());

        if (!planetScans.contains(planetScan)) {
            planetScans.add(planetScan);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Player && ((Player) o).getId() == getId();
    }
}
