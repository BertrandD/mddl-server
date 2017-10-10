package com.middlewar.core.model.buildings;

import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.items.Module;
import com.middlewar.dto.buildings.ModulableBuildingDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Data
public class ModulableBuilding extends Building {

    private List<Module> authorizedModules;
    private int maxModules;

    public ModulableBuilding(StatsSet set) {
        super(set);
        setAuthorizedModules(new ArrayList<>());
        setMaxModules(set.getInt("max_modules", 1));
    }

    @Override
    public ModulableBuildingDTO toDTO() {
        ModulableBuildingDTO dto = super.toDTO(new ModulableBuildingDTO());
        dto.setMaxModules(getMaxModules());
        dto.setAuthorizedModules(getAuthorizedModules().stream().map(Module::getItemId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public String toString() {
        return "ModulableBuilding{" +
                "authorizedModules=" + authorizedModules +
                ", maxModules=" + maxModules +
                ", " + super.toString() +
                '}';
    }
}
