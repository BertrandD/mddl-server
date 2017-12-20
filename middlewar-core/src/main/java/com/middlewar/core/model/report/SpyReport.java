package com.middlewar.core.model.report;

import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author bertrand.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpyReport extends Report {

    @ManyToOne
    private Base target;

    public SpyReport(Player owner, Base baseSrc, Base baseTarget, ReportStatus reportStatus) {
        super(owner, baseSrc, reportStatus);
        setTarget(baseTarget);
    }

    @Override
    public ReportType getType() {
        return ReportType.SPY_BASE;
    }
}
