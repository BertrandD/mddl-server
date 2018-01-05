package com.middlewar.api.manager;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface BuildingManager {

    BuildingInstance create(@NotNull Player player, int baseId, @NotEmpty String templateId);
    BuildingInstance upgrade(@NotNull Player player, int baseId, int buildingId);
    BuildingInstance attachModule(@NotNull Player player, int baseId, int buildingId, @NotEmpty String moduleId);
}
