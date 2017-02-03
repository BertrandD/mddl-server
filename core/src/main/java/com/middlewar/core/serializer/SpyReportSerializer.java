package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.report.SpyReport;

import java.io.IOException;

/**
 * @author bertrand.
 */
public class SpyReportSerializer extends JsonSerializer<SpyReport> {
    @Override
    public void serialize(SpyReport spyReport, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("baseSrcId", spyReport.getBaseSrc().getId());
        gen.writeStringField("baseTargetId", spyReport.getBaseTarget().getId());
        gen.writeObjectField("report", spyReport.getEntries());
        gen.writeEndObject();
    }
}
