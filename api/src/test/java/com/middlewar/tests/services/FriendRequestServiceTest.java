package com.middlewar.tests.services;

import com.middlewar.api.Application;
import com.middlewar.api.auth.AccountService;
import com.middlewar.api.services.FriendRequestService;
import com.middlewar.api.services.PlayerService;
import com.middlewar.core.config.Config;
import com.middlewar.core.holders.PlayerHolder;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Leboc Philippe.
 */
@RunWith(SpringRunner.class)
@Rollback
@SpringBootTest(classes = Application.class)
public class FriendRequestServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FriendRequestService friendRequestService;

    private Player _requester;
    private Player _requested;

    @Before
    public void init() {
        Config.load();
        final Account account = accountService.create("AccountTest", "no-password");
        _requester = playerService.create(account, "PlayerTest");
        _requested = playerService.create(account, "PlayerTest2");
    }

    @After
    public void destroy() {
        accountService.deleteAll();
        playerService.deleteAll();
        friendRequestService.deleteAll();
    }

    @Test
    public void shouldReturnNewCreatedFriendRequest() {

        final String message = "Hi ! I want to be your friend !";

        final FriendRequest request = friendRequestService.create(_requester, _requested, message);

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getRequester().getId()).isEqualTo(_requester.getId());
        Assertions.assertThat(request.getRequested().getId()).isEqualTo(_requested.getId());

        final FriendRequest request2 = friendRequestService.create(_requester, _requested, message);

        Assertions.assertThat(request2).isNotNull();
        Assertions.assertThat(request2.getRequester().getId()).isEqualTo(_requester.getId());
        Assertions.assertThat(request2.getRequested().getId()).isEqualTo(_requested.getId());
    }

}
