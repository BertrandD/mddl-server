package com.middlewar.core.holders;

import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class BuildingInstanceHolder extends BuildingHolder {

    private String instanceId;

    public BuildingInstanceHolder(String id, String templateId, int level) {
        super(templateId, level);
        setInstanceId(id);
    }
}
