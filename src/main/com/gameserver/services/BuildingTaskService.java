package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.tasks.BuildingTask;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingTaskService extends DatabaseService<BuildingTask> {

    protected BuildingTaskService() {
        super(BuildingTask.class);
    }

    @Override
    public BuildingTask create(Object... params) {
        if(params.length != 3) return null;

        final BuildingInstance inst = (BuildingInstance) params[0];
        final long timestamp = (long) params[1];
        final int level = (int) params[2];

        final BuildingTask buildingTask = new BuildingTask(inst.getBase(), inst, timestamp, level);
        mongoOperations.insert(buildingTask);
        return buildingTask;
    }

    public BuildingTask findFirstByOrderByEndsAtAsc() {
        final Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "endsAt"));
        query.limit(1);
        return mongoOperations.findOne(query, BuildingTask.class);
    }

    public List<BuildingTask> findByBuilding(String id) {
        return findBy(Criteria.where("building.$id").is(new ObjectId(id)));
    }
    public List<BuildingTask> findByBuildingOrderByEndsAtAsc(String id) {
        return findBy(new Sort(Sort.Direction.ASC, "endsAt"), Criteria.where("building.$id").is(new ObjectId(id)));
    }

    public List<BuildingTask> findByBaseOrderByEndsAtAsc(Base base) {
        return findBy(new Sort(Sort.Direction.ASC, "endsAt"), Criteria.where("base.$id").is(new ObjectId(base.getId())));
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtAsc(String id) {
        return findOneBy(new Sort(Sort.Direction.ASC, "endsAt"), Criteria.where("building.$id").is(new ObjectId(id)));
    }

    public BuildingTask findFirstByBuildingOrderByEndsAtDesc(String id) {
        return findOneBy(new Sort(Sort.Direction.DESC, "endsAt"), Criteria.where("building.$id").is(new ObjectId(id)));
    }
}
