package com.middlewar.controllers.ship;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.manager.impl.BaseManagerImpl;
import com.middlewar.api.manager.impl.PlayerManagerImpl;
import com.middlewar.api.manager.impl.ShipManagerImpl;
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
    ShipManagerImpl shipManager;

    @Autowired
    private PlayerManagerImpl playerManager;

    @Autowired
    private BaseManagerImpl baseManagerImpl;

    @RequestMapping(method = RequestMethod.POST)
    public Ship create(@AuthenticationPrincipal Account pAccount,
                       @RequestParam(value = "count") Long count,
                       @RequestParam(value = "structureId") String structure,
                       @RequestParam(value = "attachments") List<String> ids) {
        return shipManager.create(baseManagerImpl.getCurrentBaseOfPlayer(playerManager.getCurrentPlayerForAccount(pAccount)), count, structure, ids);
    }

}
