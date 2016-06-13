package com.gameserver.model.buildings;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.commons.StatsSet;
import com.gameserver.model.items.Module;
import com.util.data.json.View;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
public abstract class ModulableBuilding extends Building {

    private static final Logger logger = Logger.getLogger(ModulableBuilding.class.getName());

    @JsonView(View.Standard.class)
    private HashMap<String, Module> modules;

    @JsonView(View.Standard.class)
    private int maxModules;

    public ModulableBuilding(StatsSet set) {
        super(set);
        setModules(new HashMap<>());
        setMaxModules(set.getInt("max_modules", 0));

        final String itemsIdsStr = set.getString("availables_modules", null);
        if(itemsIdsStr != null){
            String[] itemsIds = itemsIdsStr.split(";");
            for (String id : itemsIds) {
                final Module item = (Module)ItemData.getInstance().getTemplate(id);
                if(item != null) addModule(item);
            }
        }
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    private void setModules(HashMap<String, Module> modules) {
        this.modules = modules;
    }

    private void addModule(Module module) {
        if(!modules.containsKey(module.getItemId())) {
            this.modules.put(module.getItemId(), module);
        } else {
            logger.warning("duplicate available Module => "+module.getItemId()+" rejected.");
        }
    }

    public int getMaxModules() {
        return maxModules;
    }

    public void setMaxModules(int maxModules) {
        this.maxModules = maxModules;
    }
}
