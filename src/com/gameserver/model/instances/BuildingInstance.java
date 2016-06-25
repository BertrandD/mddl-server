package com.gameserver.model.instances;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.Lang;
import com.gameserver.model.Base;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.items.Module;
import com.util.data.json.View;
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
public class BuildingInstance
{
    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonBackReference
    @JsonView(View.Standard.class)
    private Base base;

    @JsonView(View.Standard.class)
    private String buildingId;

    @JsonView(View.Standard.class)
    private int currentLevel;

    @JsonView(View.Standard.class)
    private long endsAt;

    @JsonView(View.Standard.class)
    private long startedAt;

    @JsonView(View.Standard.class)
    private List<String> modules;

    @Transient
    @JsonIgnore
    private Lang lang = Lang.EN;

    public BuildingInstance(){
        setModules(new ArrayList<>());
    }

    public BuildingInstance(Base base, Building template) {
        setBase(base);
        setBuildingId(template.getId());
        setCurrentLevel(0);
        setStartedAt(-1);
        setModules(new ArrayList<>());
    }

    @JsonView(View.buildingInstance_full.class)
    public Building getTemplate() {
        final Building building = BuildingData.getInstance().getBuilding(buildingId);
        building.setLang(getLang());
        return building;
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

    @JsonIgnore
    private Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public long getBuildTime() {
        return getTemplate().getBuildTimeAtLevel(getCurrentLevel());
    }

    @JsonView(View.Standard.class)
    public List<Module> getModules() {
    List<Module> all = new ArrayList<>();
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
            return (this.id.equals(building.id));
        }
        return false;
    }
}
