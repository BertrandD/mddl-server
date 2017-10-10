package com.middlewar.dto.buildings;

import com.middlewar.dto.BuildingDTO;
import lombok.Data;

import java.util.List;

@Data
public class ModulableBuildingDTO extends BuildingDTO {
    private int maxModules;
    private List<String> authorizedModules;
}
