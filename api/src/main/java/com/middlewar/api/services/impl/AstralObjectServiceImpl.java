package com.middlewar.api.services.impl;

import com.middlewar.api.dao.AstralObjectDao;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.BlackHole;
import com.middlewar.core.model.space.Moon;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leboc Philippe
 */
@Service
public class AstralObjectServiceImpl implements AstralObjectService {

    @Autowired
    private AstralObjectDao astralObjectDao;

    @Override
    public AstralObject create(String name, AstralObject parent, AstralObjectType type) {
        AstralObject object;
        switch (type)
        {
            case MOON: object = new Moon(name, parent); break;
            case PLANET: object = new Planet(name, parent); break;
            case BLACKHOLE: object = new BlackHole(name); break;
            case ASTEROID:
            case COMET:
            case STAR:
            case WORMHOLE:
            default: object = null;
        }

        // TODO: warning ABOUT parent !
        // TODO: find parent in DATABASE BEFORE OR STORE IT RECUSIVELY !!!!!
        object = astralObjectDao.insert(object);

        return object;
    }

    public void saveUniverse(AstralObject astralObject) {
        // TODO: replace me
        if (astralObject.getSatellites().size() > 0) {
            astralObject.getSatellites().forEach(this::saveUniverse);
        }
        astralObjectDao.save(astralObject);
    }

    public AstralObject findOneByName(String name) {
        return astralObjectDao.findOneByName(name);
    }

    @Override
    public AstralObject findOne(String id) {
        return astralObjectDao.findOne(id);
    }

    @Override
    public List<AstralObject> findAll() {
        return astralObjectDao.findAll();
    }

    @Override
    public void update(AstralObject object) {
        astralObjectDao.save(object);
    }

    @Override
    public void remove(AstralObject object) {
        astralObjectDao.delete(object);
    }

    @Override
    public void deleteAll() {
        astralObjectDao.deleteAll();
    }
}
