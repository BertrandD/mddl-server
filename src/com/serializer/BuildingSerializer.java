package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.ModulableBuilding;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BuildingSerializer extends JsonSerializer<Building> {
    @Override
    public void serialize(Building value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("type", value.getType().name());
        gen.writeNumberField("maxLevel", value.getMaxLevel());

        gen.writeArrayFieldStart("useEnergy");
        for(long val : value.getUseEnergy())
            gen.writeNumber(val);
        gen.writeEndArray();

        gen.writeArrayFieldStart("buildTimes");
        for(long val : value.getBuildTimes())
            gen.writeNumber(val);
        gen.writeEndArray();

        if(value instanceof ModulableBuilding) {
            gen.writeNumberField("maxModules", ((ModulableBuilding) value).getMaxModules());
            if(!((ModulableBuilding) value).getModules().isEmpty())
            {
                gen.writeArrayFieldStart("availablesModules");
                for(String moduleId : ((ModulableBuilding) value).getModules().keySet())
                    gen.writeString(moduleId);
                gen.writeEndArray();
            }
        }

        gen.writeObjectField("requirements", value.getRequirements());
        gen.writeEndObject();
    }
}
