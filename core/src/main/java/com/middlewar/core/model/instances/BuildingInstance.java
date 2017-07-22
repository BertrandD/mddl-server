package com.middlewar.core.model.instances;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.serializer.BuildingInstanceSerializer;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = BuildingInstanceSerializer.class)
public class BuildingInstance {

    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
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
