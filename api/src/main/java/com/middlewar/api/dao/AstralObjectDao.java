package com.middlewar.api.dao;

import com.middlewar.core.model.space.AstralObject;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectDao extends DefaultRepository<AstralObject, String> {
    AstralObject findOneByName(String name);
}
