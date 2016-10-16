package com.gameserver.holders;

/**
 * @author LEBOC Philippe
 */
public class BuildingInstanceHolder extends BuildingHolder {

    private String instanceId;

    public BuildingInstanceHolder(String id, String templateId, int level) {
        super(templateId, level);
        setInstanceId(id);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
