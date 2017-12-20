package com.middlewar.controllers.ship;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.ShipManager;
import com.middlewar.client.Route;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/ship", produces = "application/json")
public class ShipController {

    @Autowired
    private ShipManager shipManager;

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
