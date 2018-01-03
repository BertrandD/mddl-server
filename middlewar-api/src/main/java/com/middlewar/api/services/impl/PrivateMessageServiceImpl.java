package com.middlewar.api.services.impl;

import com.middlewar.api.services.PrivateMessageService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import com.middlewar.core.repository.PrivateMessageRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class PrivateMessageServiceImpl extends CrudServiceImpl<PrivateMessage, Integer, PrivateMessageRepository> implements PrivateMessageService {

    @Override
    public PrivateMessage create(@NotNull Player author, @NotNull Player receiver, @NotEmpty String message) {
        return repository.save(new PrivateMessage(author, receiver, message));
    }
}
