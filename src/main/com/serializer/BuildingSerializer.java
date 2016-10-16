package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.holders.StatHolder;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.ModulableBuilding;
import com.gameserver.model.buildings.ModuleFactory;
import com.gameserver.model.items.Module;

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

        if(value instanceof ModulableBuilding)
        {
            gen.writeNumberField("maxModules", ((ModulableBuilding) value).getMaxModules());
            if(!((ModulableBuilding) value).getModules().isEmpty())
            {
                gen.writeArrayFieldStart("availableModules");
                for (Module module : ((ModulableBuilding) value).getModules())
                    gen.writeString(module.getItemId());
                gen.writeEndArray();
            }
        } else {
            gen.writeNumberField("maxModules", 0);
        }

        if(!value.getStats().isEmpty())
        {
            gen.writeObjectFieldStart("stats");
            for (StatHolder holder : value.getStats()) {
                gen.writeArrayFieldStart(holder.getStat().name());
                for (double v : holder.getValues()) {
                    gen.writeNumber(v);
                }
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }

        if(value instanceof ModuleFactory)
        {
            gen.writeObjectFieldStart("unlockModules");
            for(int level : ((ModuleFactory) value).getModulesByLevel().keySet()) {
                gen.writeArrayFieldStart(""+level);
                for(Module module : ((ModuleFactory) value).getModulesByLevel(level))
                    gen.writeString(module.getItemId());
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }

        gen.writeObjectField("requirements", value.getRequirements());
        gen.writeEndObject();
    }
}
