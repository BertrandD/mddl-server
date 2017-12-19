package com.middlewar.core.model.report;

import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
@Entity
public class SpyReport extends Report {

    @ManyToOne
    private Base baseTarget;

    public SpyReport(Player owner, Base baseSrc, Base baseTarget, ReportStatus reportStatus) {
        super(owner, baseSrc, reportStatus);
        setBaseTarget(baseTarget);
    }

    @Override
    public ReportType getType() {
        return ReportType.SPY_BASE;
    }
}
