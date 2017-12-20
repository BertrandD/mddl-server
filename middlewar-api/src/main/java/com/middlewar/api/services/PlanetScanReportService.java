package com.middlewar.api.services;

import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.BaseReportEntry;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import org.springframework.stereotype.Service;

/**
 * @author bertrand.
 */
@Service
public class PlanetScanReportService implements DefaultService<PlanetScanReport> {
    private int nextId = 0;

    public PlanetScanReport create(Player owner, Base baseSrc, Planet planet) {

        // TODO : add logic for scan defense
        // TODO : send notification to scanned bases

        final PlanetScanReport report = new PlanetScanReport(owner, baseSrc, planet, ReportStatus.SUCCESS);

        owner.getCurrentBase().getReports().add(report);

        for (Base base : planet.getBases()) {
            report.addEntry(new BaseReportEntry(base), ReportCategory.BASES);
            owner.addPlanetScanned(planet, base);
        }

        return report;
    }

    @Override
    public void delete(PlanetScanReport o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PlanetScanReport findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
