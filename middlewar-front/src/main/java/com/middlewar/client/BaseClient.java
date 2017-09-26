package com.middlewar.client;

import com.middlewar.dto.BaseDTO;
import com.middlewar.dto.holder.BuildingHolderDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseClient extends APIClient {
    public static BaseDTO createBase(String name) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("name", name);

        return post(Route.BASE_CREATE + "?name={name}", BaseDTO.class, uriParams);
    }

    public static List<BuildingHolderDTO> getBuildables(BaseDTO base) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("id", ""+base.getId());
        BuildingHolderDTO[] buildings = get(Route.BASE_BUILDABLE, BuildingHolderDTO[].class, uriParams);
        if (buildings == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(buildings);
    }

    public static BaseDTO getBase(Integer currentBase) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("id", Integer.toString(currentBase));

        return get(Route.BASE_ONE, BaseDTO.class, uriParams);

    }
}
