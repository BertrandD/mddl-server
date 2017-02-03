package com.middlewar.core.model.report;

/**
 * @author bertrand.
 */
public class SpyReportEntry {

    private String name;
    private double value;

    public SpyReportEntry(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
