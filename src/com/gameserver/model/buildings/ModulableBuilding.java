package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Module;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public abstract class ModulableBuilding extends Building {

    private HashMap<String, Module> modules;
    private int maxModules;

    public ModulableBuilding(StatsSet set) {
        super(set);
        setModules(new HashMap<>());
        setMaxModules(set.getInt("max_modules", 0));
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public void setModules(HashMap<String, Module> modules) {
        this.modules = modules;
    }

    public int getMaxModules() {
        return maxModules;
    }

    public void setMaxModules(int maxModules) {
        this.maxModules = maxModules;
    }
}
