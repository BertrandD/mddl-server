package com.middlewar.api.services.impl;

import com.middlewar.api.dao.FleetDao;
import com.middlewar.api.services.FleetService;
import com.middlewar.core.enums.VehicleMission;
import com.middlewar.core.model.commons.Coordinates;
import com.middlewar.core.model.vehicles.Fleet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class FleetServiceImpl extends DefaultServiceImpl<Fleet, FleetDao> implements FleetService {

    @Autowired
    private FleetDao repository;

    @Override
    public Fleet create(Coordinates departure, Coordinates arrival, VehicleMission mission) {
        return repository.save(new Fleet(arrival, departure, mission));
    }
}
