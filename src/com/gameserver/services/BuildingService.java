package com.gameserver.services;

import com.gameserver.data.xml.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BuildingService extends DatabaseService<BuildingInstance> {

    protected BuildingService() {
        super(BuildingInstance.class);
    }

    @Override
    public BuildingInstance create(Object... params) {
        if(params.length != 2) return null;
        final Base base = (Base) params[0];
        final String buildingId = (String) params[1];

        final Building template = BuildingData.getInstance().getBuilding(buildingId);
        if(template == null) return null;

        final BuildingInstance inst = new BuildingInstance(base, buildingId);
        mongoOperations.insert(inst);
        return inst;
    }

    public BuildingInstance findBy(Base base, String id) {
        return findOneBy(Criteria.where("id").is(id), (Criteria.where("base.$id").is(new ObjectId(base.getId()))));
    }

    public List<BuildingInstance> findByBaseAndBuildingId(Base base, String buildingId) {
        return findBy(Criteria.where("base").is(new ObjectId(base.getId())).andOperator(Criteria.where("buildingId").is(buildingId)));
    }
}
