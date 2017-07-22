package com.middlewar.core.model.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
public class ReportEntry {

    private String name;
    @Transient
    private Object value;

    public ReportEntry(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
