package com.middlewar.core.model;

import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.StatCalculator;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.model.vehicles.Fleet;
import com.middlewar.core.model.vehicles.Ship;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Table(name = "bases")
public class Base {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @Size(min = 3, max = 16)
    private String name;

    @ManyToOne
    private Player owner;

    @NotNull
    @ManyToOne
    private Planet planet;

    @Transient
    private ObjectStat baseStat;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ship> ships;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fleet> fleets;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuildingInstance> buildings;

    @OneToOne(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    private BaseInventory baseInventory;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Resource> resources;

    @OneToMany(mappedBy = "baseSrc", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports;

    private PriorityQueue<BuildingTask> buildingTasks;

    public Base() {
        setBaseStat(new ObjectStat());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
        setBuildings(new ArrayList<>());
        setResources(new ArrayList<>());
        setReports(new ArrayList<>());
        setBuildingTasks(new PriorityQueue<>());
    }

    public Base(String name, Player owner, Planet planet) {
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setBaseInventory(new BaseInventory(this));
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
        setReports(new ArrayList<>());
        setPlanet(planet);
        setBuildingTasks(new PriorityQueue<>());
    }

    public long calcResourceStorageAvailableCapacity(Resource resource) {
        StatCalculator capacity = new StatCalculator(resource.getStatMax());
        capacity.add(getBaseStat().getValue(resource.getStatMax()));

        for (BuildingInstance buildingInstance : getBuildings()) {
            capacity.add(buildingInstance.calcAvailableCapacity(resource));
        }

        return ((Number) capacity.getValue()).longValue();
    }

    public double calcResourceProduction(Resource resource) {
        // TODO : add logic to handle modules effects on production
        StatCalculator production = new StatCalculator(resource.getStat());
        production.add(getBaseStat().getValue(resource.getStat()));

        for (BuildingInstance building : getBuildings()) {
            production.add(building.calcProduction(resource));
        }

        return production.getValue();
    }

    // TODO : remove that
    public void initializeStats() {
//        final ObjectStat stats = getBaseStat();
//
//        // Register base stats
//        stats.unlock(Stats.BASE_HEALTH);
//        stats.unlock(Stats.BASE_MAX_HEALTH, Config.BASE_INITIAL_MAX_HEALTH, StatOp.DIFF);
//        stats.unlock(Stats.BASE_SHIELD);
//        stats.unlock(Stats.BASE_MAX_SHIELD, Config.BASE_INITIAL_MAX_SHIELD, StatOp.DIFF);
//
//        stats.unlock(Stats.ENERGY);
//        stats.unlock(Stats.BASE_MAX_STORAGE_VOLUME, Config.BASE_INITIAL_MAX_RESOURCE_STORAGE, StatOp.DIFF);
//
//        stats.unlock(Stats.RESOURCE_1);
//        stats.unlock(Stats.RESOURCE_2);
//        stats.unlock(Stats.RESOURCE_3);
//        stats.unlock(Stats.RESOURCE_4);
//        stats.unlock(Stats.RESOURCE_5);
//
//        stats.unlock(Stats.MAX_RESOURCE_1);
//        stats.unlock(Stats.MAX_RESOURCE_2);
//        stats.unlock(Stats.MAX_RESOURCE_3);
//        stats.unlock(Stats.MAX_RESOURCE_4);
//        stats.unlock(Stats.MAX_RESOURCE_5);
//
//        // Applying module that's unlock stats.
//        final List<BuildingInstance> silos = getBuildings()
//                .stream()
//                .filter(r -> r.getTemplateId().equalsIgnoreCase("silo"))
//                .collect(Collectors.toList());
//
//        if (silos != null && !silos.isEmpty()) {
//            silos.forEach(silo -> silo.getModules().forEach(module -> module.handleEffect(stats))); // Warning: CAN'T WORK IF WE ADD OTHERS STATS THAN MAX_RESOURCE_XX UNLOCK
//        }
//
////        // Applying buildings stats
////        long energyConsumption = 0;
////        for (BuildingInstance building : getBuildings()) {
////            final Building template = building.getTemplate();
////            template.handleEffect(stats, building.getCurrentLevel());
////            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
////        }
//
////        // Building energy consumption
////        stats.add(Stats.ENERGY, -energyConsumption, StatOp.DIFF);
//
//        // Applying Module stats
//        for (BuildingInstance inst : getBuildings()) {
//            for (Module module : inst.getModules()) {
//                module.handleEffect(stats);
//            }
//        }
    }

    public void addBuilding(BuildingInstance building) {
        this.buildings.add(building);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }
}
