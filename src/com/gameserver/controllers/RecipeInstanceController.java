package com.gameserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Player;
import com.gameserver.model.instances.RecipeInstance;
import com.gameserver.services.PlayerService;
import com.gameserver.services.RecipeService;
import com.util.data.json.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@RestController
@RequestMapping(value = "/recipe", produces = "application/json")
public class RecipeInstanceController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<RecipeInstance> findAll(){
        return recipeService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RecipeInstance findRecipeInstance(@PathVariable("id") String id){
        return recipeService.findOne(id);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ArrayList<String> test(@RequestParam(value = "array") ArrayList<String> list, @RequestParam(value = "second", required = false) ArrayList<String> second){

        return second;
    }

    @JsonView(View.Standard.class)
    @RequestMapping(method = RequestMethod.POST)
    public RecipeInstance create(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "player") String player,
            @RequestParam(value = "structure") String structure,
            @RequestParam(value = "cargos") ArrayList<String> cargos,
            @RequestParam(value = "engines") ArrayList<String> engines,
            @RequestParam(value = "modules") ArrayList<String> modules,
            @RequestParam(value = "technologies") ArrayList<String> technologies,
            @RequestParam(value = "weapons") ArrayList<String> weapons){

        Player p = playerService.findOne(player);
        if(p == null) return null;

        if(ItemData.getInstance().getStructure(structure) == null){
            return null;
        }

        // TODO: add a lots of checks !!!

        RecipeInstance recipe = recipeService.create(name, p, structure, cargos, engines, modules, technologies, weapons);
        if(recipe == null) return null;
        return recipe;
    }
}
