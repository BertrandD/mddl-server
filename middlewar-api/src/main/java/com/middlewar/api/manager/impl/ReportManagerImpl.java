package com.middlewar.api.manager.impl;

import com.middlewar.api.services.BaseService;
import com.middlewar.core.exception.BaseNotFoundException;
import com.middlewar.core.exception.SpyReportCreationException;
import com.middlewar.api.manager.ReportManager;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.report.SpyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static com.middlewar.core.predicate.BasePredicate.hasId;

/**
 * @author Bertrand
 */
@Service
@Validated
public class ReportManagerImpl implements ReportManager {

    @Autowired
    private BaseService baseService;

    @Autowired
    private SpyReportServiceImpl spyReportService;

    public List<Report> getAllReportsOfCurrentPlayer(@NotNull Player player) {
        player.getCurrentBase().getReports().sort(Collections.reverseOrder());
        return player.getCurrentBase().getReports();
    }

    @Override
    public SpyReport spy(@NotNull Player player, int sourceId, int targetId) {
        final Base base = player.getBases().stream().filter(hasId(sourceId)).findFirst().orElseThrow(BaseNotFoundException::new);
        final Base baseTarget = baseService.find(targetId);

        final SpyReport report = spyReportService.create(player, base, baseTarget);
        if (report == null) throw new SpyReportCreationException();

        return report;
    }
}
