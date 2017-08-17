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

        return post("/me/base?name={name}", BaseDTO.class, uriParams);
    }

    public static List<BuildingHolderDTO> getBuildables(BaseDTO base) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("id", ""+base.getId());
        BuildingHolderDTO[] buildings = get("/me/base/{id}/buildables", BuildingHolderDTO[].class, uriParams);
        if (buildings == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(buildings);
    }
}
