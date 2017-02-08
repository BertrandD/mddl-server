package com.middlewar.api.gameserver.services;

import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.projections.BaseLight;
import org.springframework.stereotype.Service;

/**
 * @author bertrand.
 */
@Service
public class PlanetScanReportService extends DatabaseService<PlanetScanReport> {
    protected PlanetScanReportService() {
        super(PlanetScanReport.class);
    }

    @Override
    public PlanetScanReport create(Object... params) {
        if(params.length != 3) return null;

        final Player owner = (Player) params[0];
        final Base baseSrc = (Base) params[1];
        final Planet planet = (Planet) params[2];

        // TODO : add logic for scan defense
        // TODO : send notification to scanned bases

        final PlanetScanReport report = new PlanetScanReport(owner, baseSrc, planet, ReportStatus.SUCCESS);

        owner.getReports().add(report);

        for (Base base : planet.getBases()) {
            report.addEntry(base.getName(), new BaseLight(base), ReportCategory.BASES);
            owner.addPlanetScanned(planet, base);
        }

        mongoOperations.insert(report);
        mongoOperations.save(owner);
        return report;
    }
}
