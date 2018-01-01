package com.middlewar.tests.manager;


import com.middlewar.api.exceptions.BadItemException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.NotEnoughSlotsException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.RecipeManager;
import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.core.data.json.WorldData;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.tests.ApplicationTest;
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
public class RecipeManagerTest {

    @Autowired
    private BaseManager baseManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private RecipeManager recipeManager;

    private Player _playerOwner;

    @Before
    public void init() {
        WorldData.getInstance().reload();
        accountService.deleteAll();
        Account account = accountService.create("toto", "");
        _playerOwner = playerManager.createForAccount(account, "owner");
    }

    @Test
    public void shouldReturnCreatedRecipe() {
        ArrayList<String> components = new ArrayList<>();

        RecipeInstance recipe = recipeManager.create(_playerOwner, "newRecipe", "structure_test", components);

        Assertions.assertThat(recipe.getName()).isEqualTo("newRecipe");
        Assertions.assertThat(recipe.getOwner()).isEqualTo(_playerOwner);
        Assertions.assertThat(recipe.getStructure().getItemId()).isEqualTo("structure_test");
    }

    @Test
    public void shouldAddRecipeToOwner() {
        ArrayList<String> components = new ArrayList<>();

        RecipeInstance recipe = recipeManager.create(_playerOwner, "newRecipe", "structure_test", components);

        Assertions.assertThat(_playerOwner.getRecipes().contains(recipe)).isTrue();
    }

    @Test(expected = ItemNotFoundException.class)
    public void shouldCheckIfStructureExists() {
        ArrayList<String> components = new ArrayList<>();

        recipeManager.create(_playerOwner, "newRecipe", "lqksdjlqksjd", components);
    }

    @Test(expected = ItemNotFoundException.class)
    public void shouldCheckIfComponentExists() {
        ArrayList<String> components = new ArrayList<>();
        components.add("azlek");

        recipeManager.create(_playerOwner, "newRecipe", "structure_test", components);
    }

    @Test(expected = BadItemException.class)
    public void shouldCheckIfComponentIsASlotItem() {
        ArrayList<String> components = new ArrayList<>();
        components.add("structure_test");

        recipeManager.create(_playerOwner, "newRecipe", "structure_test", components);
    }

    @Test(expected = NotEnoughSlotsException.class)
    public void shouldCheckAvailableSlotsOnStructure() {
        ArrayList<String> components = new ArrayList<>();
        components.add("weapon_test");
        components.add("cargo_test");
        components.add("cargo_test");
        components.add("cargo_test");

        recipeManager.create(_playerOwner, "newRecipe", "structure_test", components);
    }

    @Test
    public void shouldAddAndComputeStats() {
        ArrayList<String> components = new ArrayList<>();
        components.add("weapon_test");
        components.add("cargo_test");
        components.add("engine_test");
        components.add("engine_test");

        RecipeInstance recipe = recipeManager.create(_playerOwner, "newRecipe", "structure_test_many_slots", components);

        Assertions.assertThat(recipe.calcDamage()).isEqualTo(500);
        Assertions.assertThat(recipe.calcCargo()).isEqualTo(500);
        Assertions.assertThat(recipe.calcPower()).isEqualTo(1000);
    }
}
