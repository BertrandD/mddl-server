package com.middlewar.core.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public class BuildingHolder {

    private String templateId;
    private int level;

    public BuildingHolder(String templateId, int level) {
        setTemplateId(templateId);
        setLevel(level);
    }

    @JsonIgnore
    public Building getTemplate() {
        return null; //BuildingData.getInstance().getBuilding(templateId); TODO
    }
}
