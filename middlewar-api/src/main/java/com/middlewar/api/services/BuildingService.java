package com.middlewar.api.services;

import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.tasks.BuildingTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingService implements DefaultService<BuildingInstance> {
    private int nextId = 0;

    public BuildingInstance create(Base base, String buildingId) {
        final Building template = BuildingData.getInstance().getBuilding(buildingId);
        if (template == null) return null;
        BuildingInstance buildingInstance = new BuildingInstance(base, buildingId);
        buildingInstance.setId(nextId());
        return buildingInstance;
    }

    public List<BuildingInstance> findByBaseAndBuildingId(Base base, String templateId) {
        ArrayList<BuildingInstance> list = new ArrayList<>();
        for (BuildingInstance b : base.getBuildings()) {
            if (b.getTemplate().getId().equals(templateId)) {
                list.add(b);
            }
        }
        for (BuildingTask b : base.getBuildingTasks()) {
            if (b.getBuilding().getTemplate().getId().equals(templateId)) {
                list.add(b.getBuilding());
            }
        }
        return list;
    }

    @Override
    public void delete(BuildingInstance o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BuildingInstance findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
