package com.middlewar.controllers.ship;

import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.NoPlayerConnectedException;
import com.middlewar.api.exceptions.PlayerNotFoundException;
import com.middlewar.api.manager.BaseManager;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.manager.RecipeManager;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.instances.RecipeInstance;
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
@RequestMapping(value = "/ship", produces = "application/json")
public class RecipeController {

    @Autowired
    RecipeManager recipeManager;

    @Autowired
    private PlayerManager playerManager;

    @Autowired
    private BaseManager baseManager;

    @RequestMapping(method = RequestMethod.POST)
    public RecipeInstance create(@AuthenticationPrincipal Account pAccount,
                                 @RequestParam(value = "count") Long count,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "structureId") String structure,
                                 @RequestParam(value = "cargos") List<String> cargos,
                                 @RequestParam(value = "cargos") List<String> engines,
                                 @RequestParam(value = "cargos") List<String> modules,
                                 @RequestParam(value = "cargos") List<String> weapons) throws NoPlayerConnectedException, PlayerNotFoundException, ItemNotFoundException {
        return recipeManager.create(playerManager.getCurrentPlayerForAccount(pAccount), name, structure, cargos, engines, modules, weapons);
    }

}
