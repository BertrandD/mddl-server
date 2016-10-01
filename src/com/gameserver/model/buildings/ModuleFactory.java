package com.gameserver.model.buildings;

import com.gameserver.data.xml.ItemData;
import com.gameserver.holders.PropertiesHolder;
import com.gameserver.holders.PropertyHolder;
import com.gameserver.holders.PropertyListHolder;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
public final class ModuleFactory extends Building {

    private static final Logger logger = Logger.getLogger(Building.class.getName());
    private HashMap<Integer, List<Module>> modulesByLevel;

    public ModuleFactory(StatsSet set) {
        super(set);
        initialize(set.getObject("propertiesByLevel", PropertiesHolder.class));
    }

    private void initialize(final PropertiesHolder properties){
        setModulesByLevel(new HashMap<>());

        if(properties == null || properties.getPropertiesByLevel() == null) {
            logger.warning("PropertyByLevel is null !");
            return;
        }

        for (Map.Entry<Integer, List<PropertyListHolder>> entry : properties.getPropertiesByLevel().entrySet()) {
            addModuleLevel(entry.getKey(), entry.getValue());
        }
    }

    public boolean hasModule(int level, String moduleId) {
        final List<Module> modules = getModulesByLevel(level);
        return modules != null && modules.stream().filter(k -> k.getItemId().equals(moduleId)).count() > 0;
    }

    public List<Module> getModulesByLevel(int level) {
        final List<Module> all = new ArrayList<>();
        for (int i = 1; i <= level; i++)
            if(getModulesByLevel().containsKey(i))
                all.addAll(getModulesByLevel().get(i));
        return all;
    }

    public HashMap<Integer, List<Module>> getModulesByLevel() {
        return modulesByLevel;
    }

    public void setModulesByLevel(HashMap<Integer, List<Module>> modulesByLevel) {
        this.modulesByLevel = modulesByLevel;
    }

    public void addModuleLevel(int level, List<PropertyListHolder> list) {
        final List<Module> modules = new ArrayList<>();
        for (PropertyListHolder propertyListHolder : list) {
            for (PropertyHolder propertyHolder : propertyListHolder.getProperties()) {
                final Module module = ItemData.getInstance().getModule(propertyHolder.getValue());
                modules.add(module);
            }
        }
        getModulesByLevel().put(level, modules);
    }
}
