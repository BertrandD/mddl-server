package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.buildings.ModulableBuilding;
import com.gameserver.model.buildings.ModuleFactory;
import com.gameserver.model.buildings.PowerFactory;
import com.gameserver.model.buildings.RobotFactory;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Module;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
                gen.writeArrayFieldStart("availablesModules");
                for(String moduleId : ((ModulableBuilding) value).getModules().keySet())
                    gen.writeString(moduleId);
                gen.writeEndArray();
            }
        }

        if(value instanceof RobotFactory)
        {
            gen.writeArrayFieldStart("cooldownReduction");
            for(double cooldown : ((RobotFactory) value).getCooldownReduction()) {
                final DecimalFormat df = new DecimalFormat("#0.0000");
                final DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
                sym.setDecimalSeparator('.');
                df.setDecimalFormatSymbols(sym);
                gen.writeNumber(df.format(cooldown));
            }
            gen.writeEndArray();
        }

        if(value instanceof PowerFactory)
        {
            gen.writeArrayFieldStart("power");
            for(long power : ((PowerFactory) value).getPower())
                gen.writeNumber(power);
            gen.writeEndArray();
        }

        if(value instanceof Storage)
        {
            gen.writeArrayFieldStart("capacity");
            for(long capacity : ((Storage) value).getCapacity())
                gen.writeNumber(capacity);
            gen.writeEndArray();
        }

        if(value instanceof Extractor)
        {
            gen.writeArrayFieldStart("produceItems");
            for(GameItem item : ((Extractor) value).getProduceItems())
                gen.writeString(item.getItemId());
            gen.writeEndArray();
        }

        if(value instanceof ModuleFactory)
        {
            gen.writeObjectFieldStart("modules");
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
