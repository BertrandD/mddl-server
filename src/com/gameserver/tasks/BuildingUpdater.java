package com.gameserver.tasks;

import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.services.BuildingService;

/**
 * @author LEBOC Philippe
 */
public final class BuildingUpdater implements Runnable{

    private BuildingService service;

    private BuildingInstance building;

    public BuildingUpdater(BuildingService service, BuildingInstance building){
        this.service = service;
        this.building = building;
    }

    @Override
    public void run() {
        if(building != null){
            building.setEndsAt(0);
            building.setCurrentLevel(building.getCurrentLevel()+1);
            service.update(building);
        }
    }
}
