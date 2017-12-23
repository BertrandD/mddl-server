package com.middlewar.dto.buildings;

import com.middlewar.dto.BuildingDto;
import lombok.Data;

import java.util.List;

@Data
public class ModulableBuildingDTO extends BuildingDto {
    private int maxModules;
    private List<String> authorizedModules;
}
