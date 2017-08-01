package com.middlewar.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.middlewar.core.holders.AstralObjectHolder;
import com.middlewar.core.holders.BaseHolder;
import com.middlewar.core.model.report.PlanetScanReport;

import java.io.IOException;

/**
 * @author bertrand.
 */
public class PlanetScanReportSerializer extends ReportSerializer<PlanetScanReport> {
    @Override
    public void serialize(PlanetScanReport report, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        super.serialize(report, gen, serializerProvider);
        gen.writeObjectField("baseSrc", new BaseHolder(report.getBaseSrc()));
        gen.writeObjectField("planet", new AstralObjectHolder(report.getPlanet()));
        gen.writeEndObject();
    }
}