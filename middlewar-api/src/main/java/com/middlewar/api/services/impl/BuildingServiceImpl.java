package com.middlewar.api.services.impl;

import com.middlewar.api.services.BuildingService;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.exception.BuildingCreationException;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.repository.BuildingInstanceRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class BuildingServiceImpl extends CrudServiceImpl<BuildingInstance, Integer, BuildingInstanceRepository> implements BuildingService {

    @Override
    public BuildingInstance create(@NotNull Base base, @NotEmpty String buildingId) {
        final Building template = BuildingData.getInstance().getBuilding(buildingId);
        if (template == null) return null;

        final BuildingInstance instance = repository.save(new BuildingInstance(base, buildingId));

        if(instance == null) throw new BuildingCreationException();

        return instance;
    }

    @Override
    public List<BuildingInstance> findByBaseAndBuildingId(@NotNull Base base, @NotEmpty String templateId) {
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
}
