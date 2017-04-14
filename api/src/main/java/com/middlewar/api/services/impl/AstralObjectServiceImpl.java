package com.middlewar.api.services.impl;

import com.middlewar.api.dao.AstralObjectDao;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.model.space.AstralObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DARBON Bertrand
 */
@Service
public class AstralObjectServiceImpl implements AstralObjectService {

    @Autowired
    private AstralObjectDao astralObjectDao;

    public void saveUniverse(AstralObject astralObject) {
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
