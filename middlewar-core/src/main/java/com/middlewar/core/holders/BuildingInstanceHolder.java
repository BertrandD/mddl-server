package com.middlewar.core.holders;

import lombok.Data;

/**
 * @author LEBOC Philippe
 */
@Data
public class BuildingInstanceHolder extends BuildingHolder {

    private long instanceId;

    public BuildingInstanceHolder(long id, String templateId, int level) {
        super(templateId, level);
        setInstanceId(id);
    }
}
