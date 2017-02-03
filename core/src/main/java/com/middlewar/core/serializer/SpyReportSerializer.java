package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.projections.BaseLight;

import java.io.IOException;
import java.util.Collections;

/**
 * @author bertrand.
 */
public class SpyReportSerializer extends JsonSerializer<SpyReport> {
    @Override
    public void serialize(SpyReport spyReport, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", spyReport.getId());
        gen.writeNumberField("date", spyReport.getDate());
        gen.writeObjectField("baseSrc", new BaseLight(spyReport.getBaseSrc()));
        gen.writeObjectField("baseTarget", new BaseLight(spyReport.getBaseTarget()));
        gen.writeObjectField("entries", spyReport.getEntries());
        gen.writeEndObject();
    }
}
