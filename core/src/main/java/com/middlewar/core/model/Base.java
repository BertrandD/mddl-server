package com.middlewar.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.config.Config;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.buildings.Building;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.ResourceInventory;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.model.stats.ObjectStat;
import com.middlewar.core.model.stats.Stats;
import com.middlewar.core.model.vehicles.Fleet;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.core.serializer.BaseSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Data
@JsonSerialize(using = BaseSerializer.class)
@Document(collection = "bases")
public final class Base
{
    @Id
    private String id;
    private String name;

    @DBRef
    @Indexed
    @JsonManagedReference
    private Player owner;

    @Transient
    private ObjectStat baseStat;

    @DBRef
    @JsonManagedReference
    private List<Ship> ships;

    @DBRef
    private List<Fleet> fleets;

    @DBRef
    @JsonManagedReference
    private List<BuildingInstance> buildings;

    @DBRef
    @JsonManagedReference
    private BaseInventory baseInventory;

    @DBRef
    @JsonManagedReference
    private ResourceInventory resources;

    @DBRef
    @JsonBackReference
    private Planet planet;

    public Base() {
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setShip(new ArrayList<>());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
    }

    public Base(String name, Player owner, Planet planet) {
        setId(new ObjectId().toString());
        setName(name);
        setOwner(owner);
        setBuildings(new ArrayList<>());
        setBaseStat(new ObjectStat());
        setResources(new ArrayList<>());
        setShips(new ArrayList<>());
        setFleets(new ArrayList<>());
        setPlanet(planet);
    }

    /**
     * Base Stats Initializer
     * Use it when you want to serialize Base or use some stats
     *      Warning: @PostContruct does not work fine with Spring & MongoDB.
     *               That's why we need initialize stats hand made
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

        // Applying module that's unlock stats.
        final List<BuildingInstance> silos = getBuildings().stream().filter(r -> r.getBuildingId().equalsIgnoreCase("silo")).collect(Collectors.toList());
        if(silos != null && !silos.isEmpty()) {
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
        this.buildingPositions.put(position, building.getId());
    }
}
