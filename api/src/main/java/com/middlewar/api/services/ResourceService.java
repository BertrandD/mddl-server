package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.Resource;

/**
 * @author Leboc Philippe.
 */
public interface ResourceService extends DefaultService<Resource> {
    Resource create(Base base, String itemId);
}
