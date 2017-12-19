package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.AstralObject;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(produces = "application/json")
public class SpaceController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AstralObjectService astralObjectService;

    @Autowired
    private PlanetScanReportService planetScanReportService;

    @RequestMapping(value = "/system", method = RequestMethod.GET)
    public Response findMySystem(@AuthenticationPrincipal Account pAccount) {
        if (pAccount.getCurrentPlayer() == 0) return new Response(SystemMessageId.CHOOSE_PLAYER);
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        final Star star = (Star) astralObjectService.findOne(player.getCurrentBase().getPlanet().getParent().getId());
        return new Response(star);
    }

    @RequestMapping(value = "/system/{id}", method = RequestMethod.GET)
    public Response findSystem(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id) {
        if (pAccount.getCurrentPlayer() == 0) return new Response(SystemMessageId.CHOOSE_PLAYER);

        Star star = (Star) astralObjectService.findOne(id);

        return new Response(star);
    }

    @RequestMapping(value = "/scan/{id}", method = RequestMethod.GET)
    public Response scanAstralObject(@AuthenticationPrincipal Account pAccount, @PathVariable("id") Long id) {
        if (pAccount.getCurrentPlayer() == 0) return new Response(SystemMessageId.CHOOSE_PLAYER);

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response(SystemMessageId.PLAYER_NOT_FOUND);

        AstralObject planet = astralObjectService.findOne(id);

        if (!(planet instanceof Planet)) {
            return new Response("Not a planet");
        }

        PlanetScanReport report = planetScanReportService.create(player, player.getCurrentBase(), (Planet) planet);
        Response response = new Response(report);
        response.getMetadata().put("player", player);
        return response;
    }
}
