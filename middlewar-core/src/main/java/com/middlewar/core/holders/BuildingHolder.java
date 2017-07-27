package com.middlewar.core.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class BuildingHolder {

    private String templateId;
    private int level;

    public BuildingHolder(String templateId, int level) {
        setTemplateId(templateId);
        setLevel(level);
    }

    @JsonIgnore
    public Building getTemplate(){
        return BuildingData.getInstance().getBuilding(templateId);
    }
}
