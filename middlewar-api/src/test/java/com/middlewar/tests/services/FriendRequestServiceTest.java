package com.middlewar.tests.services;


import com.middlewar.api.services.impl.AccountServiceImpl;
import com.middlewar.api.services.impl.FriendRequestServiceImpl;
import com.middlewar.api.services.impl.PlayerServiceImpl;
import com.middlewar.core.model.Account;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.FriendRequest;
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

/**
 * @author Leboc Philippe.
 */
@RunWith(SpringRunner.class)
@Rollback
@Transactional
@SpringBootTest(classes = ApplicationTest.class)
public class FriendRequestServiceTest {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private FriendRequestServiceImpl friendRequestService;

    private Player _requester;
    private Player _requested1;
    private Player _requested2;

    @Before
    public void init() {
        final Account account = accountService.create("AccountTest", "no-password");
        accountService.deleteAll();
        _requester = playerService.create(account, "PlayerTest");
        _requested1 = playerService.create(account, "PlayerTest1");
        _requested2 = playerService.create(account, "PlayerTest2");
    }

    @Test
    public void shouldReturnNewCreatedFriendRequest() {

        final String message = "Hi ! I want to be your friend !";

        final FriendRequest request = friendRequestService.create(_requester, _requested1, message);

        Assertions.assertThat(request).isNotNull();
        Assertions.assertThat(request.getRequester().getId()).isEqualTo(_requester.getId());
        Assertions.assertThat(request.getRequested().getId()).isEqualTo(_requested1.getId());

        final FriendRequest request2 = friendRequestService.create(_requester, _requested2, message);

        Assertions.assertThat(request2).isNotNull();
        Assertions.assertThat(request2.getRequester().getId()).isEqualTo(_requester.getId());
        Assertions.assertThat(request2.getRequested().getId()).isEqualTo(_requested2.getId());
    }

}
