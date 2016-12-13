package com.middlewar.core.model.vehicles;

import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IShip {

    Structure getStructure();
    List<Engine> getEngines();

    List<Cargo> getCargos();
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
