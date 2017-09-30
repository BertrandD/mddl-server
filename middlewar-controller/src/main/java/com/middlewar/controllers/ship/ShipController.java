package com.middlewar.controllers.ship;

import com.middlewar.api.exceptions.BaseNotFoundException;
import com.middlewar.api.exceptions.BaseNotOwnedException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerHasNoBaseException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.exceptions.RecipeNotFoundException;
import com.middlewar.api.exceptions.ShipCreationFailedException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ShipManager;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class ShipController {

    @Autowired
    ShipManager shipManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @RequestMapping(value = Route.SHIP_CREATE, method = RequestMethod.POST)
    public Ship create(@AuthenticationPrincipal Account pAccount,
                       @RequestParam(value = "count") Long count,
                       @RequestParam(value = "recipeId") Long recipeId) {
        return shipManager.create(baseManager.getCurrentBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)), count, recipeId);
    }

}
