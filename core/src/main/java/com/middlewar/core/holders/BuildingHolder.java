package com.middlewar.core.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;

/**
 * @author LEBOC Philippe
 */
public class BuildingHolder {

    private String templateId;
    private int level;

    public BuildingHolder(String templateId, int level) {
        setTemplateId(templateId);
        setLevel(level);
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public Building getTemplate(){
        return BuildingData.getInstance().getBuilding(templateId);
    }
}
