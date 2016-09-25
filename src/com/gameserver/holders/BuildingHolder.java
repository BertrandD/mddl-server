package com.gameserver.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.model.buildings.Building;

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
