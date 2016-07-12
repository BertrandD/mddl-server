package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.Lang;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.items.Module;
import com.serializer.BuildingInstanceSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Document(collection = "buildings")
@JsonSerialize(using = BuildingInstanceSerializer.class)
public class BuildingInstance
{
    @Id
    private String id;

    @DBRef
    @JsonBackReference
    private Base base;
    private String buildingId;
    private int currentLevel;
    private long endsAt;
    private long startedAt;
    private List<String> modules;

    @Transient
    private Lang lang = Lang.EN;

    public BuildingInstance(){
        setModules(new ArrayList<>());
    }

    public BuildingInstance(Base base, String templateId) {
        setId(new ObjectId().toString());
        setBase(base);
        setBuildingId(templateId);
        setCurrentLevel(0);
        setStartedAt(-1);
        setModules(new ArrayList<>());
    }

    public Building getTemplate() {
        final Building building = BuildingData.getInstance().getBuilding(buildingId);
        if(building != null)
            building.setLang(getLang());
        return building;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public long getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    /**
     * @return build time (in millis from functions) to get the next level
     */
    public long getBuildTime() {
        return getTemplate().getBuildTimeAtLevel(getCurrentLevel()+1);
    }

    public List<Module> getModules() {
        final  List<Module> all = new ArrayList<>();
        for (String module : modules) {
            Module m = ItemData.getInstance().getModule(module);
            if(m != null) all.add(m);
        }
        return all;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public void addModule(String module) {
        modules.add(module);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingInstance) {
            final BuildingInstance building = (BuildingInstance)o;
            return (this.id.equalsIgnoreCase(building.id));
        }
        return false;
    }
}
