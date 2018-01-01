package com.middlewar.controllers.statics;

import com.middlewar.api.annotations.authentication.User;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LEBOC Philippe
 */
@User
@RestController
@RequestMapping(value = "/building_static", produces = "application/json")
public class BuildingController {

    @RequestMapping(value = Route.STATIC_BUILDING_ALL, method = RequestMethod.GET)
    public Response findAll() {
        return new Response(BuildingData.getInstance().getBuildings());
    }

    @RequestMapping(value = Route.STATIC_BUILDING_ONE, method = RequestMethod.GET)
    public Response findBuilding(@PathVariable("id") String id) {
        final Building building = BuildingData.getInstance().getBuilding(id);
        if (building == null) return new Response(SystemMessageId.STATIC_BUILDING_DOESNT_EXIST);
        return new Response(building);
    }
}
