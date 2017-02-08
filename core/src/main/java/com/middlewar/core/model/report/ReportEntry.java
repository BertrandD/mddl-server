package com.middlewar.core.model.report;

/**
 * @author bertrand.
 */
public class ReportEntry {

    private String name;
    private Object value;

    public ReportEntry(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
