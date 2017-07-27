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
public class ShipsReportEntry extends ReportEntry {

    long amount;

    public ShipsReportEntry(String name, long amount) {
        super(name);
        setAmount(amount);
    }
}
