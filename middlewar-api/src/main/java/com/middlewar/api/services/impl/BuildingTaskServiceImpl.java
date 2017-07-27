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
public class BuildingTaskServiceImpl extends DefaultServiceImpl<BuildingTask, BuildingTaskDao> implements BuildingTaskService {

    @Autowired
    private BuildingTaskDao buildingTaskDao;

    @Override
    public BuildingTask create(BuildingInstance inst, long timestamp, int level) {
        return buildingTaskDao.save(new BuildingTask(inst.getBase(), inst, timestamp, level));
    }

    public BuildingTask findFirstByOrderByEndsAtAsc() {
        return buildingTaskDao.findFirstByOrderByEndsAtAsc();
    }

    public List<BuildingTask> findByBuilding(long id) {
        return buildingTaskDao.findByBuildingId(id);
    }

    public List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id) {
        return buildingTaskDao.findByBuildingOrderByEndsAtAsc(id);
    }

    public List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base) {
        return buildingTaskDao.findByBaseOrderByEndsAtAsc(base);
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtAsc(long id) {
        return buildingTaskDao.findFirstByBuildingIdOrderByEndsAtAsc(id);
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtDesc(long id) {
        return buildingTaskDao.findFirstByBuildingIdOrderByEndsAtDesc(id);
    }
}
