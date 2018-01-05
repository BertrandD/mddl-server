package com.middlewar.controllers;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.space.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/space", produces = "application/json")
public class SpaceController {

    @Autowired
    private PlanetScanReportService planetScanReportService;

    @RequestMapping(value = "/system", method = GET)
    public Response findMySystem(@AuthenticationPrincipal Account account) {
        final Player player = account.getCurrentPlayer();
        final Star star = (Star) player.getCurrentBase().getPlanet().getParent();
        return new Response(star);
    }

    @RequestMapping(value = "/system/{id}", method = GET)
    public Response findSystem(@AuthenticationPrincipal Account account, @PathVariable("id") String id) {
        final Star star = WorldData.getInstance().getStar(id);
        return new Response(star);
    }

    @RequestMapping(value = "/scan/system/{id}", method = GET)
    public Response scanAstralObject(@AuthenticationPrincipal Account account, @PathVariable("id") String id) {
        final Player player = account.getCurrentPlayer();

        final Planet planet = WorldData.getInstance().getPlanet(id);
        if (planet == null) {
            return new Response("Planet not found");
        }

        return new Response(planetScanReportService.create(player, player.getCurrentBase(), planet));
    }
}
