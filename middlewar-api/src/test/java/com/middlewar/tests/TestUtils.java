package com.middlewar.tests;

import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.InventoryService;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.stats.Stats;
import org.assertj.core.api.Assertions;

/**
 * @author Bertrand
 */
public class TestUtils {

    private static BuildingService buildingService;
    private static InventoryService inventoryService;

    public static void init(BuildingService buildingService, InventoryService inventoryService) {
        TestUtils.buildingService = buildingService;
        TestUtils.inventoryService = inventoryService;
    }

    public static BuildingInstance addBuildingToBase(Base base, String buildingId) {
        return addBuildingToBase(base, buildingId, 0);
    }

    public static BuildingInstance addBuildingToBase(Base base, String buildingId, int level) {
        BuildingInstance buildingInstance = buildingService.create(base, buildingId);
        buildingInstance.setCurrentLevel(level);
        base.addBuilding(buildingInstance);
        Assertions.assertThat(buildingInstance).isNotNull();
        return buildingInstance;
    }

    public static Resource addResourceToBase(Base base, String templateId, long amount) {
        return addResourceToBase(base, templateId, amount, 10 * amount);
    }

    public static Resource addResourceToBase(Base base, String templateId, long amount, double max) {
        Resource resource = inventoryService.createNewResource(base, templateId);
        Assertions.assertThat(resource).isNotNull();
        base.getBaseStat().add(Stats.MAX_RESOURCE_1, max);
        boolean result = inventoryService.addResource(resource, amount);
        Assertions.assertThat(result).isTrue();
        return resource;

    }

    public static ItemInstance addItemToBaseInventory(Base base, String templateId, long amount) {
        return addItemToBaseInventory(base, templateId, amount, 10000);
    }

    public static ItemInstance addItemToBaseInventory(Base base, String templateId, long amount, double maxStorageVolume) {
        base.getBaseStat().add(Stats.BASE_MAX_STORAGE_VOLUME, maxStorageVolume, StatOp.UNLOCK);
        ItemInstance item = inventoryService.addItem(base.getBaseInventory(), templateId, amount);
        Assertions.assertThat(item).isNotNull();
        return item;
    }
}
