package com.middlewar.core.model.report;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.projections.BaseReportProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author bertrand.
 */
@Entity
@Data
@NoArgsConstructor
public class BaseReportEntry extends ReportEntry {

    @OneToOne(cascade = CascadeType.ALL)
    private BaseReportProjection base;

    public BaseReportEntry(Base base) {
        super(base.getName());
        setBase(new BaseReportProjection(base));
    }

}
