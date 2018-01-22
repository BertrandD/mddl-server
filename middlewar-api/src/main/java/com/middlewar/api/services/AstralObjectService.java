package com.middlewar.api.services;

import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.Planet;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectService extends CrudService<AstralObject, Long> {
    Planet findPlanetByName(@NotEmpty final String name);
}
