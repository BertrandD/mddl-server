package com.middlewar.boot;

import com.middlewar.api.annotations.spring.profiles.Dev;
import com.middlewar.api.manager.PlayerManager;
import com.middlewar.api.services.AccountService;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author LEBOC Philippe
 */
@Dev
@Slf4j
@Component
public class DataInitializer {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerManager playerManager;

    @EventListener(ContextRefreshedEvent.class)
    public void afterPropertiesSet() {

        final Account darkAccount = accountService.create("DarkAccount", "123456789");
        final Account lightAccount = accountService.create("LightAccount", "123456789");

        final Player darkPlayer = playerManager.create(darkAccount, "Lilith");
        final Player lightPlayer = playerManager.create(lightAccount, "Anakim");

    }
}
