package com.gameserver.model.ship;

import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Structure;
import com.gameserver.model.items.Weapon;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IShip {

    Structure getStructure();

    List<Cargo> getCargos();
    List<Engine> getEngines();
    List<Module> getModules();
    List<Weapon> getWeapons();

    double getDamage();
    double getShield();
    double getHealth();
    double getVolume();
    double getWeight();

    double getMaxStorableVolume();
    double getMaxStorableWeight();

}
