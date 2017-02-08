package com.middlewar.api.gameserver.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.report.SpyReport;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.stereotype.Service;

/**
 * @author bertrand.
 */
@Service
public class SpyReportService extends DatabaseService<SpyReport> {
    protected SpyReportService() {
        super(SpyReport.class);
    }

    @Override
    public SpyReport create(Object... params) {
        if(params.length != 3) return null;

        final Player owner = (Player) params[0];
        final Base baseSrc = (Base) params[1];
        final Base baseTarget = (Base) params[2];

        // TODO : add logic for spy defense

        final SpyReport report = new SpyReport(owner, baseSrc, baseTarget, ReportStatus.SUCCESS);

        owner.getReports().add(report);

        for (Ship ship : baseTarget.getShips()) {
            //TODO : instead of getting structure, we should get the Recipe
            // It would be fun if (depending of the spy level of the source Base, the report would
            // have less or more details on the recipe...
            report.addEntry(ship.getStructure().getItemId(), ship.getCount(), ReportCategory.SHIPS);
        }

        for (ItemContainer resource : baseTarget.getResources()) {
            report.addEntry(resource.getItem().getTemplateId(), (int)resource.getItem().getCount(), ReportCategory.RESOURCES);
        }

        mongoOperations.insert(report);
        mongoOperations.save(owner);
        return report;
    }
}
