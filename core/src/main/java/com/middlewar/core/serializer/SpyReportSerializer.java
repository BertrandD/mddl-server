package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.holders.BaseHolder;

import java.io.IOException;

/**
 * @author bertrand.
 */
public class SpyReportSerializer extends ReportSerializer<SpyReport> {
    @Override
    public void serialize(SpyReport spyReport, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        super.serialize(spyReport, gen, serializerProvider);
        gen.writeObjectField("baseSrc", new BaseHolder(spyReport.getBaseSrc()));
        gen.writeObjectField("baseTarget", new BaseHolder(spyReport.getBaseTarget()));
        gen.writeEndObject();
    }
}
