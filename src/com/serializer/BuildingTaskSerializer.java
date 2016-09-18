package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.tasks.BuildingTask;

import java.io.IOException;

/**
 * @author Leboc Philippe
 */
public class BuildingTaskSerializer extends JsonSerializer<BuildingTask> {
    @Override
    public void serialize(BuildingTask value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("buildingId", value.getBuildingId());
        gen.writeStringField("buildingInstanceId", value.getBuilding().getId());
        gen.writeNumberField("level", value.getLevel());
        gen.writeNumberField("endsAt", value.getEndsAt());
        gen.writeEndObject();
    }
}
