package com.middlewar.dto.instances;

import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.items.GameItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BuildingInstanceDTO {

    private long id;
    private String buildingId;
    private int currentLevel;
    private long endsAt;
    private long startedAt;
    private List<String> modules;

    public BuildingInstanceDTO(BuildingInstance buildingInstance) {
        setId(buildingInstance.getId());
        setBuildingId(buildingInstance.getBuildingId());
        setCurrentLevel(buildingInstance.getCurrentLevel());
        setEndsAt(buildingInstance.getEndsAt());
        setStartedAt(buildingInstance.getStartedAt());
        setModules(buildingInstance.getModules().stream().map(GameItem::getItemId).collect(Collectors.toList()));
    }
}
