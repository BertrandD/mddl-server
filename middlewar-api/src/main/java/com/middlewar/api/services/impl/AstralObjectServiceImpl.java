package com.middlewar.api.services.impl;

import com.middlewar.api.dao.AstralObjectDao;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.enums.AstralObjectType;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.BlackHole;
import com.middlewar.core.model.space.Moon;
import com.middlewar.core.model.space.Planet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leboc Philippe
 */
@Service
public class AstralObjectServiceImpl extends DefaultServiceImpl<AstralObject, AstralObjectDao> implements AstralObjectService {

    @Autowired
    private AstralObjectDao repository;

    @Override
    public AstralObject create(String name, AstralObject parent, AstralObjectType type) {
        AstralObject object;
        switch (type) {
            case MOON:
                object = new Moon(name, parent);
                break;
            case PLANET:
                object = new Planet(name, parent);
                break;
            case BLACKHOLE:
                object = new BlackHole(name);
                break;
            case ASTEROID:
            case COMET:
            case STAR:
            case WORMHOLE:
            default:
                object = null;
        }

        // still usefull after migrating to hibernate ? cc @mathael
        // TODO: warning ABOUT parent !
        // TODO: find parent in DATABASE BEFORE OR STORE IT RECUSIVELY !!!!!
        object = repository.save(object);

        return object;
    }

    @Override
    public void saveUniverse() {
        AstralObject blackHole = WorldData.getInstance().getWorld();
        repository.save(blackHole);
    }

    @Override
    public AstralObject findOneByName(String name) {
        return repository.findOneByName(name);
    }

    @Override
    public void deleteAll() {
        repository.delete(WorldData.getInstance().getWorld());
        WorldData.getInstance().reload();
    }
}
