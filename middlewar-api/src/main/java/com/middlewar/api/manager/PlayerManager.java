package com.middlewar.api.manager;

import com.middlewar.core.exception.ForbiddenNameException;
import com.middlewar.core.exception.MaxPlayerCreationReachedException;
import com.middlewar.core.exception.PlayerCreationFailedException;
import com.middlewar.core.exception.UsernameAlreadyExistsException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface PlayerManager {

    /**
     * Create a Player for the given account with the given name
     *
     * @param account the account owner of the newly created Player
     * @param name    the name of the newly created Player
     * @return the newly created Player
     * @throws MaxPlayerCreationReachedException if the account has already reached the maximum number of players
     * @throws UsernameAlreadyExistsException    if the player name is already used
     * @throws ForbiddenNameException            if the name is in the name blacklist
     * @throws PlayerCreationFailedException     if the player creation failed
     */
    Player create(@NotNull @Valid final Account account, @NotEmpty final String name);
}
