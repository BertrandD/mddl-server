package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.projections.BaseLight;

import java.io.IOException;

/**
 * @author bertrand.
 */
public class SpyReportSerializer extends ReportSerializer<SpyReport> {
    @Override
    public void serialize(SpyReport spyReport, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        super.serialize(spyReport, gen, serializerProvider);
        gen.writeObjectField("baseSrc", new BaseLight(spyReport.getBaseSrc()));
        gen.writeObjectField("baseTarget", new BaseLight(spyReport.getBaseTarget()));
        gen.writeEndObject();
    }
}
