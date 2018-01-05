package com.middlewar.controllers.statics;

import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.model.buildings.Building;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/static/building", produces = "application/json")
public class BuildingController {

    @RequestMapping(method = GET)
    public Response findAll() {
        return new Response(BuildingData.getInstance().getBuildings());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Response findBuilding(@PathVariable("id") String id) {
        final Building building = BuildingData.getInstance().getBuilding(id);
        if (building == null) return new Response(SystemMessageId.STATIC_BUILDING_DOESNT_EXIST);
        return new Response(building);
    }
}
