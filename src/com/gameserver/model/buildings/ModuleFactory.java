package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.holders.PropertiesHolder;
import com.gameserver.holders.PropertyHolder;
import com.gameserver.holders.PropertyListHolder;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Module;
import com.util.data.json.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
public class ModuleFactory extends Building {

    private static final Logger logger = Logger.getLogger(Building.class.getName());

    @JsonView(View.Standard.class)
    private HashMap<Integer, List<Module>> modulesByLevel;

    public ModuleFactory(StatsSet set) {
        super(set);
        init(set);
    }

    private void init(StatsSet set){
        setModulesByLevel(new HashMap<>());

        final PropertiesHolder properties = set.getObject("propertiesByLevel", PropertiesHolder.class);
        if(properties == null || properties.getPropertiesByLevel() == null) {
            logger.warning("PropertyByLevel is null !");
            return;
        }

        for (Map.Entry<Integer, List<PropertyListHolder>> entry : properties.getPropertiesByLevel().entrySet()) {
            addModuleLevel(entry.getKey(), entry.getValue());
        }

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
