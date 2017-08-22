package com.middlewar.client;

import com.middlewar.dto.instances.BuildingInstanceDTO;

import java.util.HashMap;
import java.util.Map;

public class BuildingClient extends APIClient {
    public static BuildingInstanceDTO create(long baseId, String templateId) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("baseId", Long.toString(baseId));
        uriParams.put("building", templateId);

        return post(Route.BUILDING_CREATE + "?building={building}", BuildingInstanceDTO.class, uriParams);
    }

}
