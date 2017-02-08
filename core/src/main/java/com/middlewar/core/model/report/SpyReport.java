package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.serializer.SpyReportSerializer;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author bertrand.
 */
@JsonSerialize(using = SpyReportSerializer.class)
public class SpyReport extends Report{

    @DBRef
    @JsonBackReference
    private Base baseSrc;

    @DBRef
    @JsonBackReference
    private Base baseTarget;

    public SpyReport(Player owner, Base baseSrc, Base baseTarget, ReportStatus reportStatus) {
        super(owner, reportStatus);
        setBaseSrc(baseSrc);
        setBaseTarget(baseTarget);
    }


    public Base getBaseSrc() {
        return baseSrc;
    }

    public void setBaseSrc(Base baseSrc) {
        this.baseSrc = baseSrc;
    }

    public Base getBaseTarget() {
        return baseTarget;
    }

    public void setBaseTarget(Base baseTarget) {
        this.baseTarget = baseTarget;
    }

    @Override
    public ReportType getType() {
        return ReportType.SPY_BASE;
    }
}
