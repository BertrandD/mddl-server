package com.middlewar.controllers.ship;

import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.RecipeManager;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.instances.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(produces = "application/json")
public class RecipeController {

    @Autowired
    RecipeManager recipeManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @RequestMapping(value = "/me/recipe", method = RequestMethod.POST)
    public Recipe create(@AuthenticationPrincipal Account pAccount,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "structureId") String structure,
                         @RequestParam(value = "cargos") List<String> cargos,
                         @RequestParam(value = "engines") List<String> engines,
                         @RequestParam(value = "modules") List<String> modules,
                         @RequestParam(value = "weapons") List<String> weapons) throws NoPlayerConnectedException, PlayerNotFoundException, ItemNotFoundException {
        return recipeManager.create(playerManager.getCurrentPlayerForAccount(pAccount), name, structure, cargos, engines, modules, weapons);
    }

}
