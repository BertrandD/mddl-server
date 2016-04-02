package com.gameserver.services;

import com.gameserver.repository.BaseRepository;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseService {

    @Autowired
    BaseRepository repository;

    public Base findOne(String id){
        return repository.findOne(id);
    }

    public Collection<Base> findAll(){
        return repository.findAll();
    }

    public Base create(String name, Player player){
        Base b = new Base(name, player);
        return repository.save(b);
    }

    public void update(Base b) { repository.save(b); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }
}
