package com.gameserver.model.vehicles;

import com.gameserver.enums.VehicleMission;
import com.gameserver.model.commons.Coordinates;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "fleets")
public class Fleet {

    @Id
    private String id;

    private Coordinates departure;

    private Coordinates arrival;

    private VehicleMission mission;

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
}
