package com.middlewar.api.services.impl;

import com.middlewar.api.services.PlanetScanReportService;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.BaseReportEntry;
import com.middlewar.core.model.report.PlanetScanReport;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.repository.PlanetScanReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author bertrand.
 */
@Service
@Validated
public class PlanetScanReportServiceImpl extends CrudServiceImpl<PlanetScanReport, Integer, PlanetScanReportRepository> implements PlanetScanReportService {

    @Override
    public PlanetScanReport create(@NotNull Player owner, @NotNull Base baseSrc, @NotNull Planet planet) {

        // TODO : add logic for scan defense
        // TODO : send notification to scanned bases

        final PlanetScanReport report = repository.save(new PlanetScanReport(owner, baseSrc, planet, ReportStatus.SUCCESS));

        owner.getCurrentBase().getReports().add(report);

        for (Base base : planet.getBases()) {
            report.addEntry(new BaseReportEntry(base), ReportCategory.BASES);
            owner.addPlanetScanned(planet, base);
        }

        return report;
    }
}
