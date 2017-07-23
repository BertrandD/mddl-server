package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BuildingTaskDao;
import com.middlewar.api.services.BuildingTaskService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingTaskServiceImpl implements BuildingTaskService {

    @Autowired
    private BuildingTaskDao buildingTaskDao;

    @Override
    public BuildingTask create(BuildingInstance inst, long timestamp, int level) {
        return buildingTaskDao.save(new BuildingTask(inst.getBase(), inst, timestamp, level));
    }

    public BuildingTask findFirstByOrderByEndsAtAsc() {
        return buildingTaskDao.findFirstByOrderByEndsAtAsc();
    }

    public List<BuildingTask> findByBuilding(String id) {
        return buildingTaskDao.findByBuilding(id);
    }
    public List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id) {
        return buildingTaskDao.findByBuildingOrderByEndsAtAsc(id);
    }

    public List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base) {
        return buildingTaskDao.findByBaseOrderByEndsAtAsc(base);
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id) {
        return buildingTaskDao.findFirstByBuildingOrderByEndsAtAsc(id);
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtDesc(long id) {
        return buildingTaskDao.findFirstByBuildingOrderByEndsAtDesc(id);
    }

    @Override
    public BuildingTask findOne(long id) {
        return buildingTaskDao.findOne(id);
    }

    @Override
    public List<BuildingTask> findAll() {
        return buildingTaskDao.findAll();
    }

    @Override
    public void update(BuildingTask object) {
        buildingTaskDao.save(object);
    }

    @Override
    public void remove(BuildingTask object) {
        buildingTaskDao.delete(object);
    }

    @Override
    public void deleteAll() {
        buildingTaskDao.deleteAll();
    }
}
