package com.middlewar.api.services.impl;

import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.repository.AstralObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Leboc Philippe.
 */
@Slf4j
@Service
public class AstralObjectServiceImpl extends CrudServiceImpl<AstralObject, Long, AstralObjectRepository> implements AstralObjectService{
    @Override
    public Planet findPlanetByName(final String name) {
        return repository.findDistinctByName(name);
    }
}
