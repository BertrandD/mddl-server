package com.gameserver.model.buildings;

import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public abstract class ModulableBuilding extends Building {

    private List<Module> modules;
    private int maxModules;

    public ModulableBuilding(StatsSet set) {
        super(set);
        setModules(new ArrayList<>());
        setMaxModules(set.getInt("max_modules", 0));
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public int getMaxModules() {
        return maxModules;
    }

    public void setMaxModules(int maxModules) {
        this.maxModules = maxModules;
    }
}
