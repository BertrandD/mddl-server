package com.middlewar.core.model.report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.enums.ReportCategory;
import com.middlewar.core.enums.ReportStatus;
import com.middlewar.core.enums.ReportType;
import com.middlewar.core.model.Player;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author bertrand.
 */
public abstract class Report implements Comparable<Report> {

    @Id
    private String id;

    private long date;

    @DBRef
    @JsonBackReference
    private Player owner;

    private HashMap<ReportCategory, List<ReportEntry>> entries;

    private ReportStatus reportStatus;

    public abstract ReportType getType();

    public Report(Player owner, ReportStatus reportStatus) {
        setId(new ObjectId().toString());
        setDate(System.currentTimeMillis());
        setOwner(owner);
        setEntries(new HashMap<>());
        setReportStatus(reportStatus);
    }

    public void addEntry(String name, Object value, ReportCategory category) {
        ReportEntry reportEntry = new ReportEntry(name, value);
        if (!entries.containsKey(category)) {
            entries.put(category, new ArrayList<>());
        }
        entries.get(category).add(reportEntry);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public HashMap<ReportCategory, List<ReportEntry>> getEntries() {
        return entries;
    }

    public void setEntries(HashMap<ReportCategory, List<ReportEntry>> entries) {
        this.entries = entries;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    @Override
    public int compareTo(Report o) {
        if (getDate() < o.getDate()) {
            return -1;
        } else if (getDate() == o.getDate()) {
            return 0;
        }
        return -1;
    }
}
