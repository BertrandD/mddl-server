package com.middlewar.api.services.impl;

import com.middlewar.core.exception.BaseNotFoundException;
import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.space.Planet;
import com.middlewar.core.repository.BaseRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class BaseServiceImpl extends CrudServiceImpl<Base, Integer, BaseRepository> implements BaseService {

    @Autowired
    private PlayerService playerService;

    @Override
    public Base create(@NotEmpty String name, @NotNull Player player, @NotNull Planet planet) {

        final Base base = repository.save(new Base(name, player, planet));

        if(base != null) {
            player.addBase(base);
            player.setCurrentBase(base);
            planet.addBase(base);
            playerService.update(player);
        }

        return base;
    }

    @Override
    public Base find(Integer integer) {
        final Base base = repository.findOne(integer);
        if (base == null) throw new BaseNotFoundException();
        return base;
    }
}
