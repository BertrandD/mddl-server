package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;

/**
 * @author Leboc Philippe.
 */
public interface BaseService extends DefaultService<Base> {
    Base create(String name, Player player);
}
