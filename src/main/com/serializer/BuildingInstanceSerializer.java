package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.items.Module;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class BuildingInstanceSerializer extends JsonSerializer<BuildingInstance> {

    @Override
    public void serialize(BuildingInstance value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("buildingId", value.getBuildingId());
        gen.writeStringField("baseId", value.getBase().getId());
        gen.writeNumberField("currentLevel", value.getCurrentLevel());
        gen.writeNumberField("startedAt", value.getStartedAt());
        gen.writeNumberField("endsAt", value.getEndsAt());

        if(!value.getModules().isEmpty()) {
            gen.writeArrayFieldStart("modules");
            for (Module module : value.getModules())
                gen.writeString(module.getItemId());
            gen.writeEndArray();
        }

        gen.writeEndObject();
    }
}
