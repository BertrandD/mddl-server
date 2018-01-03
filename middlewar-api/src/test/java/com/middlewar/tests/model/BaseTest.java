package com.middlewar.tests.model;

import com.middlewar.core.exceptions.ForbiddenNameException;
import com.middlewar.core.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.core.exceptions.NoPlayerConnectedException;
import com.middlewar.core.exceptions.PlayerCreationFailedException;
import com.middlewar.core.exceptions.PlayerNotFoundException;
import com.middlewar.core.exceptions.UsernameAlreadyExistsException;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.space.Planet;
import com.middlewar.tests.ApplicationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class BaseTest {

    private Base _base;

    @Before
    public void init() {
        _base = new Base("base", new Player(new Account(), "toto"), new Planet());
    }

    @Test
    public void shouldComputeResourceCapacity() {
        Resource resource = new Resource(_base, new ItemInstance(_base.getBaseInventory(), "resource_1", 0));
        BuildingInstance buildingInstance = new BuildingInstance(_base, "silo");
        buildingInstance.setCurrentLevel(1);
        buildingInstance.addModule("module_silo_improve_1");
        buildingInstance.addModule("module_silo_improve_1_2");
        buildingInstance.addModule("module_silo_improve_1_2");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(_base.calcResourceStorageAvailableCapacity(resource)).isEqualTo(2050);
    }


    @Test
    public void shouldComputeResourceProduction() {
        Resource resource = new Resource(_base, new ItemInstance(_base.getBaseInventory(), "resource_1", 0));
        BuildingInstance buildingInstance = new BuildingInstance(_base, "mine");
        buildingInstance.setCurrentLevel(1);
        buildingInstance.addModule("module_optimizer_1");
        buildingInstance.addModule("module_optimizer_1");
        buildingInstance.addModule("module_optimizer_1_2");
        _base.addBuilding(buildingInstance);
        Assertions.assertThat(_base.calcResourceProduction(resource)).isEqualTo(2050);
    }
}
