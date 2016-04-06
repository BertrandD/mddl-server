package com.gameserver.model.vehicles;

import com.gameserver.enums.VehicleState;
import org.springframework.data.annotation.Id;

/**
 * @author LEBOC Philippe
 */
public abstract class Vehicle {

    @Id
    private String id;

    private VehicleState state;

    public Vehicle(){}

    public String getId() {
        return id;
    }

    public abstract long getMaxSpeed();

    public void setId(String id) {
        this.id = id;
    }

    public VehicleState getState() {
        return state;
    }

    public void setState(VehicleState state) {
        this.state = state;
    }
}
