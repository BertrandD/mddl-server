package com.middlewar.tests.manager;


import com.middlewar.core.exceptions.ItemRequirementMissingException;
import com.middlewar.core.exceptions.RecipeNotFoundException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.RecipeManager;
import com.middlewar.api.manager.ShipManager;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.BuildingServiceImpl;
import com.middlewar.api.services.impl.InventoryServiceImpl;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;

/**
 * @author Bertrand
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class ShipManagerTest {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private RecipeManager recipeManager;

    @Autowired
    private BuildingServiceImpl buildingService;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private ShipManager shipManager;

    private Player _playerOwner;
    private Base _base;
    private RecipeInstance _recipe;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();
        TestUtils.init(buildingService, inventoryService);
        Account account = accountService.create("toto", "");
        _playerOwner = playerManager.create(account, "owner");
        _base = baseManager.create(_playerOwner, "owner");
        ArrayList<String> components = new ArrayList<>();
        components.add("weapon_test");
        components.add("cargo_test");
        _recipe = recipeManager.create(_playerOwner, "newRecipe", "structure_test_many_slots", components);
    }

    @Test
    public void shouldReturnCreatedShip() {
        RecipeInstance recipe = recipeManager.create(_playerOwner, "newRecipe2", "structure_test", new ArrayList<>());

        TestUtils.addItemToBaseInventory(_base, "structure_test", 1);

        /*Ship ship = shipManager.createFriendRequest(_base, 1L, recipe.getId());

        Assertions.assertThat(ship).isNotNull();
        Assertions.assertThat(ship.getBase()).isEqualTo(_base);
        Assertions.assertThat(ship.getStructure().getItemId()).isEqualTo("structure_test");*/
    }

    @Test(expected = RecipeNotFoundException.class)
    public void shouldcheckIfRecipeExists() {
        //shipManager.createFriendRequest(_base, 1L, 10000);
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughStructures() {
       // shipManager.createFriendRequest(_base, 1L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughStructures2() {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        //shipManager.createFriendRequest(_base, 2L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughItems() {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        //shipManager.createFriendRequest(_base, 1L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughItems2() {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        //shipManager.createFriendRequest(_base, 1L, _recipe.getId());
    }

    @Test
    public void shouldConsumeStructuresAndItems() {
        ItemInstance structure_test = TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 2);
        ItemInstance weapon_test = TestUtils.addItemToBaseInventory(_base, "weapon_test", 2);
        ItemInstance cargo_test = TestUtils.addItemToBaseInventory(_base, "cargo_test", 2);

       /* shipManager.createFriendRequest(_base, 2L, _recipe.getId());

        Assertions.assertThat(weapon_test.getCount()).isEqualTo(0);
        Assertions.assertThat(cargo_test.getCount()).isEqualTo(0);
        Assertions.assertThat(structure_test.getCount()).isEqualTo(0);*/
    }

}
