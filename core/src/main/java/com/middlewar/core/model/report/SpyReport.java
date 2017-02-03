package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.SpyReportCategory;
import com.middlewar.core.enums.SpyReportStatus;
import com.middlewar.core.model.Base;
import com.middlewar.core.serializer.SpyReportSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand.
 */
@JsonSerialize(using = SpyReportSerializer.class)
public class SpyReport {

    @Id
    private String id;

    @DBRef
    @JsonBackReference
    private Base baseSrc;

    @DBRef
    @JsonBackReference
    private Base baseTarget;

    private SpyReportStatus reportStatus;

    private HashMap<SpyReportCategory, List<SpyReportEntry>> entries;

    public SpyReport(Base baseSrc, Base baseTarget, SpyReportStatus reportStatus) {
        setId(new ObjectId().toString());
        setBaseSrc(baseSrc);
        setBaseTarget(baseTarget);
        setReportStatus(reportStatus);
        setEntries(new HashMap<>());
    }

    public void addEntry(String name, double value, SpyReportCategory category) {
        SpyReportEntry reportEntry = new SpyReportEntry(name, value);
        if (!entries.containsKey(category)) {
            entries.put(category, new ArrayList<>());
        }
        entries.get(category).add(reportEntry);
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

    public SpyReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(SpyReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public HashMap<SpyReportCategory, List<SpyReportEntry>> getEntries() {
        return entries;
    }

    public void setEntries(HashMap<SpyReportCategory, List<SpyReportEntry>> entries) {
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
