package com.middlewar.core.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@Entity
@Data
@NoArgsConstructor
public class ResourcesReportEntry extends ReportEntry {

    double amount;

    public ResourcesReportEntry(String name, double amount) {
        super(name);
        setAmount(amount);
    }
}
