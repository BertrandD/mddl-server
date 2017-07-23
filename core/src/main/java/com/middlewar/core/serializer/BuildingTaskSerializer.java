package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.tasks.BuildingTask;

import java.io.IOException;

/**
 * @author Leboc Philippe
 */
public class BuildingTaskSerializer extends JsonSerializer<BuildingTask> {
    @Override
    public void serialize(BuildingTask value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeObjectField("building", value.getBuilding());
        gen.writeNumberField("endsAt", value.getEndsAt());
        gen.writeEndObject();
    }
}
