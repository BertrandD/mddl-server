package com.middlewar.api.services;

import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface PlayerService extends CrudService<Player, Integer> {
    Player create(@NotNull Account account, @NotEmpty String name);
}
