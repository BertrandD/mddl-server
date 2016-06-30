package com.gameserver.services;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.instances.BuildingInstance;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

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
        return findOneBy(new Criteria[]{Criteria.where("id").is(id), (Criteria.where("base").is(base))});
    }

    public BuildingInstance findByBaseAndBuildingId(Base base, String buildingId) {
        return findOneBy(new Criteria[]{Criteria.where("base").is(base), (Criteria.where("buildingId").is(buildingId))});
    }
}
