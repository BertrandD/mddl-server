package com.middlewar.api.gameserver.services;

import com.middlewar.core.model.space.AstralObject;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author DARBON Bertrand
 */
@Service
public class AstralObjectService extends DatabaseService<AstralObject> {

    protected AstralObjectService() {
        super(AstralObject.class);
    }

    @Override
    public AstralObject create(Object... params) {
        throw new NotImplementedException();
    }

    public void saveUniverse(AstralObject astralObject) {
        if (astralObject.getSatellites().size() > 0) {
            astralObject.getSatellites().forEach(this::saveUniverse);
        }
        mongoOperations.save(astralObject);
    }
}
