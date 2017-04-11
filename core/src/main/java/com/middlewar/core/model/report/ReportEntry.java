package com.middlewar.core.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
public class ReportEntry {

    private String name;
    private Object value;

    public ReportEntry(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
