package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.vehicles.Ship;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface ShipService extends DefaultService<Ship> {
    Ship create(Base base, String structure, long count, List<String> ids);
}
