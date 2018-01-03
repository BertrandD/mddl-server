package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface RecipeService extends CrudService<RecipeInstance, Integer> {
    RecipeInstance create(@NotEmpty String name, @NotNull Player owner, @NotNull Structure structure, @NotEmpty List<SlotItem> components);
}
