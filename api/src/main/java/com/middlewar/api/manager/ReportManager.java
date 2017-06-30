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
    private PlayerManager playerManager;

    @Autowired
    private SpyReportServiceImpl spyReportServiceImpl;

    public List<Report> getAllReportsOfCurrentPlayer(Account account) throws NoPlayerConnectedException, PlayerNotFoundException {
        final Player player = playerManager.getCurrentPlayerForAccount(account);
        player.getReports().sort(Collections.reverseOrder());
        return player.getReports();

    }

    public SpyReport spy(Account account, String baseId) throws BaseNotFoundException, NoPlayerConnectedException, PlayerNotFoundException, SpyReportCreationException {
        final Base baseTarget = baseManager.getBase(baseId);

        final Player player = playerManager.getCurrentPlayerForAccount(account);

        final SpyReport report = spyReportServiceImpl.create(player, player.getCurrentBase(), baseTarget);
        if(report == null) throw new SpyReportCreationException();

        return report;
    }
}
