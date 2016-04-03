package com.gameserver.services;

import com.gameserver.model.Player;
import com.gameserver.model.instances.RecipeInstance;
import com.gameserver.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class RecipeService {

    @Autowired
    RecipeRepository repository;

    public RecipeInstance findOne(String id){
        return repository.findOne(id);
    }

    public List<RecipeInstance> findAll() {
        return repository.findAll();
    }

    public RecipeInstance create(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> technologies, List<String> weapons){
        return repository.save(new RecipeInstance(name, owner, structureId, cargos, engines, modules, technologies, weapons));
    }

    public void update(RecipeInstance p){ repository.save(p); }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void delete(String id){
        repository.delete(id);
    }

}
