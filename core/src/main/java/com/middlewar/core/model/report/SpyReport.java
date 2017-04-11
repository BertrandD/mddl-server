package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.serializer.SpyReportSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author bertrand.
 */
@JsonSerialize(using = SpyReportSerializer.class)
@Data
@NoArgsConstructor
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

    @Override
    public ReportType getType() {
        return ReportType.SPY_BASE;
    }
}
