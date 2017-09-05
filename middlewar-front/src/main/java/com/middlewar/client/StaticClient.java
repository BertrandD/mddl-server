package com.middlewar.client;

import com.middlewar.dto.BuildingDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticClient extends APIClient {

    public static List<BuildingDTO> getBuildings() {
        BuildingDTO[] buildings = get(Route.STATIC_BUILDING_ALL, BuildingDTO[].class, null);
        if (buildings == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(buildings);
    }

}
