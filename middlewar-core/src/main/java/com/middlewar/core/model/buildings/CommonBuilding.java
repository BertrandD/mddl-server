package com.middlewar.core.model.buildings;

import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.dto.BuildingDTO;

/**
 * @author LEBOC Philippe
 */
public class CommonBuilding extends Building {
    public CommonBuilding(StatsSet set) {
        super(set);
    }

    @Override
    public BuildingDTO toDTO() {
        return super.toDTO(new BuildingDTO());
    }
}
