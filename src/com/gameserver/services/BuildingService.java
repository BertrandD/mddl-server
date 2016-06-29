package com.gameserver.services;

import com.config.Config;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.manager.BuildingTaskManager;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.RobotFactory;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.tasks.BuildingTask;
import com.gameserver.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingService {

    @Autowired
    private BuildingRepository repository;

    @Autowired
    private BuildingTaskService buildingTaskService;

    @Autowired
    private BuildingTaskManager buildingTaskManager;

    public BuildingInstance findOne(String id){
        return repository.findOne(id);
    }

    public BuildingInstance findByBaseAndId(Base base, String id) { return repository.findByBaseAndId(base, id); }

    public BuildingInstance findByBaseAndBuildingId(Base base, String buildingId) { return repository.findByBaseAndBuildingId(base, buildingId); }

    public List<BuildingInstance> findAll() {
        return repository.findAll();
    }

    public BuildingInstance create(Base base, String buildingId){
        final Building b = BuildingData.getInstance().getBuilding(buildingId);
        if(b == null) return null;
        final BuildingInstance p = new BuildingInstance(base, b);
        return repository.save(p);
    }

    public void update(BuildingInstance p) {
        repository.save(p);
    }

    public void ScheduleUpgrade(BuildingInstance building) {
        final BuildingTask newTask;
        final long now = System.currentTimeMillis();
        final BuildingInstance robotFactory = building.getBase().getBuildings().stream().filter(k -> k.getCurrentLevel() > 0 &&
                k.getTemplate().getType().equals(BuildingCategory.RobotFactory)).findFirst().orElse(null);
        final BuildingTask lastInQueue = buildingTaskService.findFirstByBuildingOrderByEndsAtDesc(building.getId());

        // calculate BuildTime with modifiers :
        // - World modifier
        // - RobotFactory modifier
        long buildTime = (long)(building.getBuildTime() * Config.BUILDTIME_MODIFIER);
        long endupgrade = now + buildTime;

        if(robotFactory != null) endupgrade -= (long)(buildTime * (((RobotFactory)robotFactory.getTemplate()).getCoolDownReductionAtLevel(robotFactory.getCurrentLevel())));

        if(lastInQueue == null){
            building.setStartedAt(now); // This value is a false startedAt value ! Difference of ~30 millis
            building.setEndsAt(endupgrade);
            update(building);
            newTask = buildingTaskService.create(building, endupgrade, building.getCurrentLevel()+1);
        }else{
            endupgrade = lastInQueue.getEndsAt() + (long)(building.getBuildTime() * Config.BUILDTIME_MODIFIER);
            newTask = buildingTaskService.create(building, endupgrade, lastInQueue.getLevel()+1);
        }

        buildingTaskManager.notifyNewTask(newTask);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void delete(String id){
        repository.delete(id);
    }

}
