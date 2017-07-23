package com.middlewar.api.dao;

import com.middlewar.core.model.space.AstralObject;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectDao extends DefaultRepository<AstralObject, Long> {
    AstralObject findOneByName(String name);
}
