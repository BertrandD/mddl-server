package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.Player;

/**
 * @author Leboc Philippe.
 */
public interface BaseService extends DefaultService<Base> {
    Base create(String name, Player player);
}
