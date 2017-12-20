package com.middlewar.core.model.buildings;

import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.items.Module;
import com.middlewar.dto.BuildingDTO;

/**
 * @author LEBOC Philippe
 */
public final class ModuleFactory extends ItemFactory<Module> {
    public ModuleFactory(StatsSet set) {
        super(set);
    }

    @Override
    public BuildingDTO toDTO() {
        return super.toDTO();
    }
}
