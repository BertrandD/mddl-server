package com.gameserver.services;

import com.gameserver.enums.VehicleMission;
import com.gameserver.model.commons.Coordinates;
import com.gameserver.model.vehicles.Fleet;

/**
 * @author LEBOC Philippe
 */
public class FleetService extends DatabaseService<Fleet> {

    protected FleetService() {
        super(Fleet.class);
    }

    @Override
    public Fleet create(Object... params) {
        if(params.length != 3) return null;
        final Coordinates arrival = (Coordinates) params[0];
        final Coordinates departure = (Coordinates) params[1];
        final VehicleMission mission = (VehicleMission) params[2];

        final Fleet fleet = new Fleet(arrival, departure, mission);
        mongoOperations.insert(fleet);
        return fleet;
    }
}
