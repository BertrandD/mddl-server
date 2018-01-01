package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface BaseService extends CrudService<Base, Integer> {
    Base create(@NotEmpty String name, @NotNull Player player, @NotNull Planet planet);
}
