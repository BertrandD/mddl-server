package com.middlewar.api.manager;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.api.util.response.Response;
import com.middlewar.api.util.response.SystemMessageId;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.report.SpyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Bertrand
 */
@Service
public class ReportManager {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private SpyReportServiceImpl spyReportServiceImpl;

    public List<Report> getAllReportsOfCurrentPlayer(Player player) throws NoPlayerConnectedException, PlayerNotFoundException {
        player.getReports().sort(Collections.reverseOrder());
        return player.getReports();

    }

    /**
     * @param player owner of the spy report
     * @param baseId source of the spy mission
     * @param target target of the spy mission
     * @return the spy report
     * @throws BaseNotFoundException if one of the given base id is not found
     * @throws BaseNotOwnedException if the source base is now owned by the given player
     * @throws SpyReportCreationException if something went wrong
     */
    public SpyReport spy(Player player, String baseId, String target) throws BaseNotFoundException, BaseNotOwnedException, SpyReportCreationException {
        final Base base = baseManager.getOwnedBase(baseId, player);
        final Base baseTarget = baseManager.getBase(target);

        final SpyReport report = spyReportServiceImpl.create(player, base, baseTarget);
        if(report == null) throw new SpyReportCreationException();

        return report;
    }
}
