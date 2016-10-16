package com.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Weapon;
import com.gameserver.model.vehicles.Ship;

import java.io.IOException;

/**
 * @author LEBOC Philippe
 */
public class ShipSerializer extends JsonSerializer<Ship> {
    @Override
    public void serialize(Ship value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("id", value.getId());
        gen.writeStringField("baseId", value.getBase().getId());
        gen.writeNumberField("count", value.getCount());
        gen.writeStringField("state", value.getState().name());
        gen.writeObjectField("structure", value.getStructure());

        gen.writeArrayFieldStart("engines");
        for (Engine engine : value.getEngines()) {
            gen.writeObject(engine);
        }
        gen.writeEndArray();

        if(!value.getCargos().isEmpty()){
            gen.writeArrayFieldStart("cargos");
            for (Cargo cargo : value.getCargos()) {
                gen.writeObject(cargo);
            }
            gen.writeEndArray();
        }

        if(!value.getModules().isEmpty()){
            gen.writeArrayFieldStart("modules");
            for (Module module : value.getModules()) {
                gen.writeObject(module);
            }
            gen.writeEndArray();
        }

        if(!value.getWeapons().isEmpty()){
            gen.writeArrayFieldStart("weapons");
            for (Weapon weapon : value.getWeapons()) {
                gen.writeObject(weapon);
            }
            gen.writeEndArray();
        }

        gen.writeEndObject();
    }
}
