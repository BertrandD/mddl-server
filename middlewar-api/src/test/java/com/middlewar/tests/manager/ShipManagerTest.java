package com.middlewar.tests.manager;


import com.middlewar.api.exceptions.BadItemException;
import com.middlewar.api.exceptions.BaseCreationException;
import com.middlewar.api.exceptions.ForbiddenNameException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.MaxPlayerCreationReachedException;
import com.middlewar.api.exceptions.NotEnoughSlotsException;
import com.middlewar.api.exceptions.PlayerCreationFailedException;
import com.middlewar.api.exceptions.RecipeCreationFailedException;
import com.middlewar.api.exceptions.RecipeNotFoundException;
import com.middlewar.api.exceptions.ShipCreationFailedException;
import com.middlewar.api.exceptions.UsernameAlreadyExistsException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.RecipeManager;
import com.middlewar.api.manager.ShipManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.services.BuildingService;
import com.middlewar.api.services.InventoryService;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.tests.ApplicationTest;
import com.middlewar.tests.TestUtils;
import org.assertj.core.api.Assertions;
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
    private AccountService accountService;

    @Autowired
    private RecipeManager recipeManager;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ShipManager shipManager;

    private Player _playerOwner;
    private Base _base;
    private RecipeInstance _recipe;

    @Before
    public void init() throws MaxPlayerCreationReachedException, ForbiddenNameException, PlayerCreationFailedException, UsernameAlreadyExistsException, ItemNotFoundException, NotEnoughSlotsException, RecipeCreationFailedException, BadItemException, BaseCreationException {
        WorldData.getInstance().reload();
        accountService.deleteAll();
        TestUtils.init(buildingService, inventoryService);
        Account account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(account, "owner");
        _base = baseManager.create(_playerOwner, "owner");
        ArrayList<String> components = new ArrayList<>();
        components.add("weapon_test");
        components.add("cargo_test");
        _recipe = recipeManager.create(_playerOwner, "newRecipe", "structure_test_many_slots", components);
    }

    @Test
    public void shouldReturnCreatedShip() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException, ItemNotFoundException, NotEnoughSlotsException, RecipeCreationFailedException, BadItemException {
        RecipeInstance recipe = recipeManager.create(_playerOwner, "newRecipe2", "structure_test", new ArrayList<>());

        TestUtils.addItemToBaseInventory(_base, "structure_test", 1);

        Ship ship = shipManager.create(_base, 1L, recipe.getId());

        Assertions.assertThat(ship).isNotNull();
        Assertions.assertThat(ship.getBase()).isEqualTo(_base);
        Assertions.assertThat(ship.getStructure().getItemId()).isEqualTo("structure_test");
    }

    @Test(expected = RecipeNotFoundException.class)
    public void shouldcheckIfRecipeExists() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        shipManager.create(_base, 1L, 10000);
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughStructures() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        shipManager.create(_base, 1L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughStructures2() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        shipManager.create(_base, 2L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughItems() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        shipManager.create(_base, 1L, _recipe.getId());
    }

    @Test(expected = ItemRequirementMissingException.class)
    public void shouldcheckIfEnoughItems2() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 1);
        shipManager.create(_base, 1L, _recipe.getId());
    }

    @Test
    public void shouldConsumeStructuresAndItems() throws ShipCreationFailedException, ItemRequirementMissingException, RecipeNotFoundException {
        ItemInstance structure_test = TestUtils.addItemToBaseInventory(_base, "structure_test_many_slots", 2);
        ItemInstance weapon_test = TestUtils.addItemToBaseInventory(_base, "weapon_test", 2);
        ItemInstance cargo_test = TestUtils.addItemToBaseInventory(_base, "cargo_test", 2);

        shipManager.create(_base, 2L, _recipe.getId());

        Assertions.assertThat(weapon_test.getCount()).isEqualTo(0);
        Assertions.assertThat(cargo_test.getCount()).isEqualTo(0);
        Assertions.assertThat(structure_test.getCount()).isEqualTo(0);
    }

}
