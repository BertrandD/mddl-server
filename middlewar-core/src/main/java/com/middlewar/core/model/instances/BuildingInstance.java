package com.middlewar.core.model.instances;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.stats.StatCalculator;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.serializer.BuildingInstanceSerializer;
import com.middlewar.dto.instances.BuildingInstanceDTO;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = BuildingInstanceSerializer.class)
public class BuildingInstance {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Base base;
    private String buildingId;
    private int currentLevel;
    private long endsAt;
    private long startedAt;

    @ElementCollection
    private List<String> modules;

    @Transient
    private Lang lang = Lang.EN;

    public BuildingInstance() {
        setModules(new ArrayList<>());
    }

    public BuildingInstance(Base base, String templateId) {
        setBase(base);
        setBuildingId(templateId);
        setCurrentLevel(0);
        setStartedAt(-1);
        setModules(new ArrayList<>());
    }

    public BuildingInstanceDTO toDTO() {
        BuildingInstanceDTO dto = new BuildingInstanceDTO();
        dto.setId(this.getId());
        dto.setBuildingId(this.getBuildingId());
        dto.setCurrentLevel(this.getCurrentLevel());
        dto.setEndsAt(this.getEndsAt());
        dto.setStartedAt(this.getStartedAt());
        dto.setModules(this.getModules().stream().map(GameItem::getItemId).collect(Collectors.toList()));
        return dto;
    }

    public Building getTemplate() {
        final Building building = BuildingData.getInstance().getBuilding(buildingId);
        if (building != null)
            building.setLang(getLang());
        return building;
    }

    /**
     * @return build time (in millis from functions) to get the next level
     */
    public long getBuildTime() {
        return getTemplate().getBuildTimeAtLevel(getCurrentLevel() + 1);
    }

    public List<Module> getModules() {
        final List<Module> all = new ArrayList<>();
        for (String module : modules) {
            Module m = ItemData.getInstance().getModule(module);
            if (m != null) all.add(m);
        }
        return all;
    }

    public void addModule(String module) {
        modules.add(module);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BuildingInstance) {
            final BuildingInstance building = (BuildingInstance) o;
            return (this.id == building.id);
        }
        return false;
    }

    public StatHolder calcAvailableCapacity(Resource resource) {

        // If the buildingInstance has 3 modules :
        // 2 modules giving +50% each of bonus (so a total of +100%)        <-- calcModulesModifier()
        // 1 module giving a raw +50 capacity bonus (StatOp.DIFF)           <-- calcModulesBonus()
        //     |-> N.B. : Raw bonuses are not affected by other modules
        // And the basic availableCapacity of the building is 1000
        // So, the formula is : 1000 * (1 + 0.5 + 0.5) + 50 = 2050

        StatCalculator capacity = new StatCalculator(resource.getStatMax());
        capacity.add(getTemplate().calcAvailableCapacity(resource, getCurrentLevel()));
        capacity.add(calcModulesModifier(resource.getStatMax()));
        capacity.add(calcModulesBonus(resource.getStatMax()));
        return capacity.toStatHolder();
    }

    public StatHolder calcProduction(Resource resource) {

        // Cf calcAvailableCapacity for some explanations on formula

        StatCalculator production = new StatCalculator(resource.getStat());
        production.add(getTemplate().calcProductionAtLevel(resource, getCurrentLevel()));
        production.add(calcModulesModifier(resource.getStat()));
        production.add(calcModulesBonus(resource.getStat()));
        return production.toStatHolder();
    }

    private StatHolder calcModulesModifier(Stats stats) {
        StatCalculator capacity = new StatCalculator(stats);
        capacity.add(1, StatOp.DIFF);

        for (Module module : getModules()) {
            List<StatHolder> statHolders = module
                    .getAllStats()
                    .stream()
                    .filter(
                            k -> k.getStat().equals(stats) && k.getOp().equals(StatOp.PER)
                    )
                    .collect(Collectors.toList());
            for (StatHolder stat : statHolders) {
                capacity.add(stat.getValue() - 1, StatOp.DIFF);
            }
        }

        return capacity.toStatHolder(StatOp.PER);
    }

    private StatHolder calcModulesBonus(Stats stats) {
        StatCalculator capacity = new StatCalculator(stats);

        for (Module module : getModules()) {
            List<StatHolder> statHolders = module
                    .getAllStats()
                    .stream()
                    .filter(
                            k -> k.getStat().equals(stats) && k.getOp().equals(StatOp.DIFF)
                    )
                    .collect(Collectors.toList());
            for (StatHolder stat : statHolders) {
                capacity.add(stat);
            }
        }

        return capacity.toStatHolder(StatOp.DIFF);
    }
}
