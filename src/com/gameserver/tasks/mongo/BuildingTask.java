package com.gameserver.tasks.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Base;
import com.gameserver.model.instances.BuildingInstance;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "tasks")
public class BuildingTask {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonIgnore
    private Base base;

    @DBRef
    @JsonView(View.Standard.class)
    private BuildingInstance building;

    @JsonView(View.Standard.class)
    private long endsAt;

    @JsonView(View.Standard.class)
    private int level;

    public BuildingTask(){}

    public BuildingTask(Base base, BuildingInstance building, long endsAt, int level){
        setBase(base);
        setBuilding(building);
        setEndsAt(endsAt);
        setLevel(level);
    }

    @JsonView(View.Standard.class)
    public String getBuildingId(){
        return building.getBuildingId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public BuildingInstance getBuilding() {
        return building;
    }

    public void setBuilding(BuildingInstance building) {
        this.building = building;
    }

    public long getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingTask) {
            final BuildingTask task = (BuildingTask)o;
            return (this.id.equals(task.id));
        }
        return false;
    }

    public long compareToAsc(BuildingTask task) {
        return this.endsAt - task.getEndsAt();
    }

    public long compareToDesc(BuildingTask task){
        return task.getEndsAt() - this.endsAt;
    }
}
