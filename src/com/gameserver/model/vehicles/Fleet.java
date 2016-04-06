package com.gameserver.model.vehicles;

import com.gameserver.enums.VehicleMission;
import com.gameserver.interfaces.IFleet;
import com.gameserver.model.commons.Coordinates;
import com.gameserver.model.inventory.FleetInventory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Fleet implements IFleet {

    @Id
    private String id;

    private Coordinates departure;

    private Coordinates arrival;

    private VehicleMission mission;

    @DBRef
    private FleetInventory inventory;

    @DBRef
    private List<Ship> ships;

    public Fleet(){
        setShips(new ArrayList<>());
    }

    public Fleet(Coordinates from, Coordinates to, VehicleMission mission){
        setShips(new ArrayList<>());
        setDeparture(from);
        setArrival(to);
        setMission(mission);
    }

    @Override
    public long getMaxFleetStorageCapacity() {
        return getInventory().getMaxCapacity();
    }

    @Override
    public long getSpeed() {
        long power = 99999999; // Config.ENGINE_MAX_POWER;
        for(Ship ship : getShips())
        {
            if(power > ship.getMaxSpeed()) power = ship.getMaxSpeed();
        }
        return power;
    }

    public void addShip(Ship ship){
        getShips().add(ship); // TODO: add check
        // TODO: Set VehicleState = FLYING
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinates getDeparture() {
        return departure;
    }

    public void setDeparture(Coordinates departure) {
        this.departure = departure;
    }

    public Coordinates getArrival() {
        return arrival;
    }

    public void setArrival(Coordinates arrival) {
        this.arrival = arrival;
    }

    public VehicleMission getMission() {
        return mission;
    }

    public void setMission(VehicleMission mission) {
        this.mission = mission;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public FleetInventory getInventory() {
        return inventory;
    }

    public void setInventory(FleetInventory inventory) {
        this.inventory = inventory;
    }
}
