package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.serializer.PlanetScanReportSerializer;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author bertrand.
 */
@JsonSerialize(using = PlanetScanReportSerializer.class)
public class PlanetScanReport extends Report {

    @DBRef
    @JsonBackReference
    private Base baseSrc;

    @DBRef
    @JsonBackReference
    private Planet planet;

    public PlanetScanReport(Player owner, Base baseSrc, Planet planet, ReportStatus reportStatus) {
        super(owner, reportStatus);
        setBaseSrc(baseSrc);
        setPlanet(planet);
    }

    public Base getBaseSrc() {
        return baseSrc;
    }

    public void setBaseSrc(Base baseSrc) {
        this.baseSrc = baseSrc;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    @Override
    public ReportType getType() {
        return ReportType.PLANET_SCAN;
    }
}