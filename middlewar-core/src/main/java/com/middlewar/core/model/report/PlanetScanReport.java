package com.middlewar.core.model.report;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.serializer.PlanetScanReportSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author bertrand.
 */
@Entity
@Data
@NoArgsConstructor
@JsonSerialize(using = PlanetScanReportSerializer.class)
public class PlanetScanReport extends Report {

    @ManyToOne
    private Planet planet;

    public PlanetScanReport(Player owner, Base baseSrc, Planet planet, ReportStatus reportStatus) {
        super(owner, baseSrc, reportStatus);
        setPlanet(planet);
    }

    @Override
    public ReportType getType() {
        return ReportType.PLANET_SCAN;
    }
}