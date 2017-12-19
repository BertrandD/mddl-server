package com.middlewar.api.manager;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.vehicles.Ship;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface ShipManager {
    Ship create(Base base, Long count, String structure, List<String> ids);
}
