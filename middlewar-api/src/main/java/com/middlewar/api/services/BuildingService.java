package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface BuildingService extends CrudService<BuildingInstance, Integer> {
    BuildingInstance create(@NotNull Base base, @NotEmpty String buildingId);
    List<BuildingInstance> findByBaseAndBuildingId(@NotNull Base base, @NotEmpty String templateId);
}
