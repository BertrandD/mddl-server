package com.middlewar.controllers;

import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.client.Route;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class SpaceController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlanetScanReportService planetScanReportService;

    @RequestMapping(value = Route.SPACE_SYSTEM_MY, method = RequestMethod.GET)
    public Response findMySystem(@AuthenticationPrincipal Account pAccount) {
        if (pAccount.getCurrentPlayer() == 0) return new Response<>(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(SystemMessageId.PLAYER_NOT_FOUND);

        Star star = (Star) player.getCurrentBase().getPlanet().getParent();

        return new Response<>(star);
    }

    @RequestMapping(value = Route.SPACE_SYSTEM_ONE, method = RequestMethod.GET)
    public Response findSystem(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        if (pAccount.getCurrentPlayer() == 0) return new Response<>(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);

        Star star = (Star) WorldData.getInstance().getStar(id);

        return new Response<>(star);
    }

    @RequestMapping(value = Route.SPACE_SYSTEM_SCAN, method = RequestMethod.GET)
    public Response scanAstralObject(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        if (pAccount.getCurrentPlayer() == 0) return new Response<>(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if (player == null) return new Response<>(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        Planet planet = WorldData.getInstance().getPlanet(id);

        if (planet == null) {
            return new Response<>(JsonResponseType.ERROR, "Planet not found");
        }

        PlanetScanReport report = planetScanReportService.create(player, player.getCurrentBase(), (Planet) planet);
        Response response = new Response<>(report);
        response.addMeta("player", player);
        return response;
    }

}
