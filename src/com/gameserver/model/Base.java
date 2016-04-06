package com.gameserver.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.vehicles.Ship;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "player_bases")
public class Base
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @JsonView(View.Standard.class)
    private String name;

    // TODO: Generate Coordinates

    @DBRef
    @JsonView(View.Base_Onwer.class)
    private Player owner;

    @DBRef
    @JsonView({View.Base_Buildings.class})
    private List<BuildingInstance> buildings;

    @DBRef
    private List<Ship> ships;

    public Base(){
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
    }

    public Base(String name, Player owner)
    {
        setId(null);
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setShips(new ArrayList<>());
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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<BuildingInstance> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingInstance> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(BuildingInstance building) {
        this.buildings.add(building); // TODO: add check
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }
}
