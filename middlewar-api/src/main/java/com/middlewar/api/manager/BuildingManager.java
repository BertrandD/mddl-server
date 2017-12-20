package com.middlewar.api.manager;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;

/**
 * @author Leboc Philippe.
 */
public interface BuildingManager {

    /**
     *
     * @param base
     * @param id
     * @return
     */
    BuildingInstance getBuilding(Base base, long id);

    /**
     *
     * @param base
     * @param templateId
     * @return
     */
    BuildingInstance create(Base base, String templateId);

    /**
     *
     * @param base
     * @param id
     * @return
     */
    BuildingInstance upgrade(Base base, long id);

    /**
     *
     * @param base
     * @param buildingInstId
     * @param moduleId
     * @return
     */
    BuildingInstance attachModule(Base base, long buildingInstId, String moduleId);
}
