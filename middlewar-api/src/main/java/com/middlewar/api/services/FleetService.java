package com.middlewar.api.services;

import com.middlewar.api.dao.FleetDao;
import com.middlewar.core.enums.VehicleMission;
import com.middlewar.core.model.commons.Coordinates;
import com.middlewar.core.model.vehicles.Fleet;

/**
 * @author Leboc Philippe.
 */
public interface FleetService extends DefaultService<Fleet, FleetDao> {
    Fleet create(Coordinates departure, Coordinates arrival, VehicleMission mission);
}
