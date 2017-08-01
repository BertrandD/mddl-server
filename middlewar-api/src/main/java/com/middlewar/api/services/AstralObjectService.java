package com.middlewar.api.services;

import com.middlewar.api.dao.AstralObjectDao;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.space.AstralObject;

/**
 * @author Leboc Philippe.
 */
public interface AstralObjectService extends DefaultService<AstralObject, AstralObjectDao> {
    AstralObject create(String name, AstralObject parent, AstralObjectType type);

    AstralObject findOneByName(String name);

    void saveUniverse();
}