package com.middlewar.api.manager;

import com.middlewar.api.exceptions.*;
import com.middlewar.api.services.impl.SpyReportServiceImpl;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.report.SpyReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Bertrand
 */
@Service
public class SpyReportManager {

    private final BaseManager baseManager;

    private final PlayerManager playerManager;

    private final SpyReportServiceImpl spyReportServiceImpl;

    @Autowired
    public SpyReportManager(BaseManager baseManager, PlayerManager playerManager, SpyReportServiceImpl spyReportServiceImpl) {
        this.baseManager = baseManager;
        this.playerManager = playerManager;
        this.spyReportServiceImpl = spyReportServiceImpl;
    }

    public SpyReport spy(Account account, String baseId) throws BaseNotFoundException, NoPlayerConnectedException, PlayerNotFoundException, SpyReportCreationException {
        final Base baseTarget = baseManager.getBase(baseId);

        final Player player = playerManager.getCurrentPlayerForAccount(account);

        final SpyReport report = spyReportServiceImpl.create(player, player.getCurrentBase(), baseTarget);
        if(report == null) throw new SpyReportCreationException();

        return report;
    }
}
