package com.middlewar.core.holders;

import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@AllArgsConstructor
public class BuildingHolder {

    private String templateId;
    private int level;

    public Building getTemplate() {
        return BuildingData.getInstance().getBuilding(templateId);
    }
}
