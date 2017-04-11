package com.middlewar.api.controllers;

import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.api.util.response.JsonResponse;
import com.middlewar.api.util.response.JsonResponseType;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.AstralObject;
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
    private AstralObjectService astralObjectService;

    @Autowired
    private PlanetScanReportService planetScanReportService;

    @RequestMapping(value = "/system", method = RequestMethod.GET)
    public JsonResponse findMySystem(@AuthenticationPrincipal Account pAccount){
        if(pAccount.getCurrentPlayer() == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);
        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(SystemMessageId.PLAYER_NOT_FOUND);

        Star star = (Star) astralObjectService.findOne(player.getCurrentBase().getPlanet().getParent().getId());

        return new JsonResponse(star);
    }

    @RequestMapping(value = "/system/{id}", method = RequestMethod.GET)
    public JsonResponse findSystem(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        if(pAccount.getCurrentPlayer() == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);

        Star star = (Star) astralObjectService.findOne(id);

        return new JsonResponse(star);
    }

    @RequestMapping(value = "/scan/{id}", method = RequestMethod.GET)
    public JsonResponse scanAstralObject(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        if(pAccount.getCurrentPlayer() == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.CHOOSE_PLAYER);

        final Player player = playerService.findOne(pAccount.getCurrentPlayer());
        if(player == null) return new JsonResponse(JsonResponseType.ERROR, SystemMessageId.PLAYER_NOT_FOUND);

        AstralObject planet = astralObjectService.findOne(id);

        if (!(planet instanceof Planet)) {
            return new JsonResponse(JsonResponseType.ERROR, "Not a planet");
        }

        PlanetScanReport report = planetScanReportService.create(player, player.getCurrentBase(), planet);
        JsonResponse response = new JsonResponse(report);
        response.addMeta("player", player);
        return response;
    }

}
