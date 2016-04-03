package com.gameserver.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.BuildingInstance;
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

    @DBRef
    @JsonView(View.Base_Onwer.class)
    private Player owner;

    @DBRef
    @JsonView(View.Base_OwnerAndBuildings.class)
    private List<BuildingInstance> buildings;

    // TODO: store ships

    // TODO: store ItemInstance here ?

    public Base(){
        buildings = new ArrayList<>();
    }

    public Base(String name, Player owner)
    {
        setId(null);
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
    }

    public Base(String id, String name, Player owner, List<BuildingInstance> buildings){
        setId(id);
        setName(name);
        setOwner(owner);
        setBuildings(buildings);
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
}
