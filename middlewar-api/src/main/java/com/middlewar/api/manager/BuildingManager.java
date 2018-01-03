package com.middlewar.api.manager;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

/**
 * @author Leboc Philippe.
 */
public interface BuildingManager {

    BuildingInstance create(Base base, String templateId);
    BuildingInstance upgrade(Base base, long id);
    BuildingInstance attachModule(Base base, long buildingInstId, String moduleId);
}
