package com;

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

    private Player requester;
    private Player requested;

    @Before
    public void init() {
        Config.load();
        final Account account = accountService.create("FriendRequestTest", "password");
        requester = playerService.create(account, "Alpha");
        requested = playerService.create(account, "Beta");
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

        final FriendRequest request = friendRequestService.create(requester, requested, message);

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getRequester().getId()).isEqualTo(requester.getId());
        Assertions.assertThat(request.getRequested().getId()).isEqualTo(requested.getId());

        final FriendRequest request2 = friendRequestService.create(new PlayerHolder(requester), new PlayerHolder(requested), message);

        Assertions.assertThat(request2).isNotNull();
        Assertions.assertThat(request2.getRequester().getId()).isEqualTo(requester.getId());
        Assertions.assertThat(request2.getRequested().getId()).isEqualTo(requested.getId());
    }

}
