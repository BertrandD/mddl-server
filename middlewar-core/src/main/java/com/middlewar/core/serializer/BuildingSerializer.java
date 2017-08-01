package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.buildings.ModulableBuilding;
import com.middlewar.core.model.buildings.ModuleFactory;
import com.middlewar.core.model.buildings.StructureFactory;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;

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
        for (long val : value.getUseEnergy())
            gen.writeNumber(val);
        gen.writeEndArray();

        gen.writeArrayFieldStart("buildTimes");
        for (long val : value.getBuildTimes())
            gen.writeNumber(val);
        gen.writeEndArray();

        if (value instanceof ModulableBuilding) {
            gen.writeNumberField("maxModules", ((ModulableBuilding) value).getMaxModules());
            if (!((ModulableBuilding) value).getModules().isEmpty()) {
                gen.writeArrayFieldStart("availableModules");
                for (Module module : ((ModulableBuilding) value).getModules())
                    gen.writeString(module.getItemId());
                gen.writeEndArray();
            }
        } else {
            gen.writeNumberField("maxModules", 0);
        }

//        if (!value.getAllStats().isEmpty()) {
//            gen.writeObjectFieldStart("stats");
//            for (StatHolder holder : value.getAllStats()) {
//                gen.writeArrayFieldStart(holder.getStat().name());
//                for (double v : holder.getValues()) {
//                    gen.writeNumber(v);
//                }
//                gen.writeEndArray();
//            }
//            gen.writeEndObject();
//        }

        if (value instanceof ModuleFactory) {
            gen.writeObjectFieldStart("unlockModules");
            for (int level : ((ModuleFactory) value).getItemsByLevel().keySet()) {
                gen.writeArrayFieldStart("" + level);
                for (Module module : ((ModuleFactory) value).getItemsByLevel(level))
                    gen.writeString(module.getItemId());
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }


        if (value instanceof StructureFactory) {
            gen.writeObjectFieldStart("unlockStructures");
            for (int level : ((StructureFactory) value).getItemsByLevel().keySet()) {
                gen.writeArrayFieldStart("" + level);
                for (Structure structure : ((StructureFactory) value).getItemsByLevel(level))
                    gen.writeString(structure.getItemId());
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }

        gen.writeObjectField("requirements", value.getRequirements());
        gen.writeEndObject();
    }
}
