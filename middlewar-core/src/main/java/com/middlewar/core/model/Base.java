package com.middlewar.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.config.Config;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.holders.StatHolder;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.report.Report;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.vehicles.Fleet;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.core.serializer.BaseSerializer;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Data
@JsonSerialize(using = BaseSerializer.class)
@Entity
public class Base {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @ManyToOne
    private Player owner;

    @Transient
    private ObjectStat baseStat;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ship> ships;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fleet> fleets;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuildingInstance> buildings;

    @OneToOne(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BaseInventory baseInventory;

    @OneToMany(mappedBy = "base", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources;

    @OneToMany(mappedBy = "baseSrc", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports;

    @ManyToOne
    private Planet planet;

    public Base() {
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setShips(new ArrayList<>());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
        setResources(new ArrayList<>());
        setReports(new ArrayList<>());
    }

    public Base(String name, Player owner, Planet planet) {
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
        setReports(new ArrayList<>());
        setPlanet(planet);
    }

    @PreRemove
    private void cleanCurrentBase() {
        if (this.getOwner().getCurrentBase().getId() == this.getId()) {
            this.getOwner().setCurrentBase(null);
        }
    }

    public double getResourceProduction(Resource resource) {
        double production = getBaseStat().getValue(resource.getStat());
        for (BuildingInstance building : getBuildings()) {
            StatHolder statProd = building
                    .getTemplate()
                    .getStats(
                            resource.getStat()
                    );
            if (statProd != null) {
                production += statProd.getValue(building.getCurrentLevel());

            }
            // TODO : production modifiers /!\ (modules, etc)
        }

        return production;
    }

    /**
     * TODO : We don't use MongoDB anymore so what ?
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     * Warning: @PostContruct does not work fine with Spring & MongoDB.
     * That's why we need initialize stats hand made
     */
    public void initializeStats() {
        final ObjectStat stats = getBaseStat();

        // Register base stats
        stats.addStat(Stats.BASE_HEALTH);
        stats.addStat(Stats.BASE_MAX_HEALTH);
        stats.addStat(Stats.BASE_SHIELD);
        stats.addStat(Stats.BASE_MAX_SHIELD);

        stats.addStat(Stats.ENERGY);
        stats.addStat(Stats.BASE_MAX_STORAGE_VOLUME);

        stats.addStat(Stats.RESOURCE_1);
        stats.addStat(Stats.RESOURCE_2);
        stats.addStat(Stats.RESOURCE_3);
        stats.addStat(Stats.RESOURCE_4);
        stats.addStat(Stats.RESOURCE_5);

        stats.addStat(Stats.MAX_RESOURCE_1);
        stats.addStat(Stats.MAX_RESOURCE_2);
        stats.addStat(Stats.MAX_RESOURCE_3);
        stats.addStat(Stats.MAX_RESOURCE_4);
        stats.addStat(Stats.MAX_RESOURCE_5);

        // Applying module that's unlock stats.
        final List<BuildingInstance> silos = getBuildings()
                .stream()
                .filter(r -> r.getBuildingId().equalsIgnoreCase("silo"))
                .collect(Collectors.toList());

        if (silos != null && !silos.isEmpty()) {
            silos.forEach(silo -> silo.getModules().forEach(module -> module.handleEffect(stats))); // Warning: CAN'T WORK IF WE ADD OTHERS STATS THAN MAX_RESOURCE_XX UNLOCK
        }

        // Applying buildings stats
        long energyConsumption = 0;
        for (BuildingInstance building : getBuildings()) {
            final Building template = building.getTemplate();
            template.handleEffect(stats, building.getCurrentLevel());
            energyConsumption += (template.getUseEnergyAtLevel(building.getCurrentLevel()) * Config.USE_ENERGY_MODIFIER);
        }

        // Building energy consumption
        stats.add(Stats.ENERGY, -energyConsumption, StatOp.DIFF);

        // Applying Module stats
        for (BuildingInstance inst : getBuildings()) {
            for (Module module : inst.getModules()) {
                module.handleEffect(stats);
            }
        }
    }

    public void addBuilding(BuildingInstance building) {
        this.buildings.add(building);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }
}
