package com.middlewar.core.model.instances;

import com.middlewar.core.data.xml.BuildingData;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.stats.StatCalculator;
import com.middlewar.core.model.stats.Stats;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Table(name = "buildings")
public class BuildingInstance {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    private Base base;

    @NotEmpty
    private String templateId;

    @Min(0)
    private int currentLevel;

    private long endsAt;

    private long startedAt;

    @ElementCollection
    private List<String> modules;

    public BuildingInstance() {
        setModules(emptyList());
    }

    public BuildingInstance(Base base, String templateId) {
        setBase(base);
        setTemplateId(templateId);
        setCurrentLevel(0);
        setStartedAt(-1);
        setModules(emptyList());
    }

    public Building getTemplate() {
        return BuildingData.getInstance().getBuilding(templateId);
    }

    public long getBuildTime() {
        return getTemplate().getBuildTimeAtLevel(getCurrentLevel() + 1);
    }

    public List<Module> getModules() {
        return modules.stream().map(ItemData.getInstance()::getModule).collect(toList());
    }

    public void addModule(String module) {
        modules.add(module);
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
                    .filter(k -> k.getStat().equals(stats) && k.getOp().equals(StatOp.PER))
                    .collect(toList());
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
                    .filter(k -> k.getStat().equals(stats) && k.getOp().equals(StatOp.DIFF))
                    .collect(toList());
            for (StatHolder stat : statHolders) {
                capacity.add(stat);
            }
        }

        return capacity.toStatHolder(StatOp.DIFF);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof BuildingInstance && ((BuildingInstance) o).getId() == getId();
    }
}
