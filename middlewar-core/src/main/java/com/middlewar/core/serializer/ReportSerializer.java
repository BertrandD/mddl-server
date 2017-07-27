package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.report.Report;

import java.io.IOException;

/**
 * @author bertrand.
 */
public class ReportSerializer<T extends Report> extends JsonSerializer<T> {
    @Override
    public void serialize(T report, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeNumberField("id", report.getId());
        gen.writeStringField("type", report.getType().toString());
        gen.writeNumberField("date", report.getDate());
        gen.writeObjectField("reportStatus", report.getReportStatus());
        gen.writeObjectField("entries", report.getEntries());
    }
}
