package com.middlewar.controllers.statics;

import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.client.Route;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.dto.BuildingDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class BuildingController {

    @RequestMapping(value = Route.STATIC_BUILDING_ALL, method = RequestMethod.GET)
    public List<BuildingDTO> findAll(@AuthenticationPrincipal Account pAccount) {
        return BuildingData.getInstance().getBuildings(pAccount.getLang()).stream().map(Building::toDTO).collect(Collectors.toList());
    }

    @RequestMapping(value = Route.STATIC_BUILDING_ONE, method = RequestMethod.GET)
    public Response findBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id) {
        final Building b = BuildingData.getInstance().getBuilding(id);
        if (b == null) return new Response<>(pAccount.getLang(), SystemMessageId.STATIC_BUILDING_DOESNT_EXIST);
        b.setLang(pAccount.getLang());
        return new Response<>(b);
    }
}
