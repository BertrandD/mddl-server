package com.gameserver.controllers.statics;

import com.middlewar.core.model.Account;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import com.util.response.SystemMessageId;
import com.util.response.JsonResponse;
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
@RequestMapping(value = "/building_static", produces = "application/json")
public class BuildingController {

    @RequestMapping(method = RequestMethod.GET)
    public JsonResponse findAll(@AuthenticationPrincipal Account pAccount){
        return new JsonResponse(BuildingData.getInstance().getBuildings(pAccount.getLang()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse findBuilding(@AuthenticationPrincipal Account pAccount, @PathVariable("id") String id){
        final Building b = BuildingData.getInstance().getBuilding(id);
        if(b == null) return new JsonResponse(pAccount.getLang(), SystemMessageId.STATIC_BUILDING_DOESNT_EXIST);
        b.setLang(pAccount.getLang());
        return new JsonResponse(b);
    }
}
