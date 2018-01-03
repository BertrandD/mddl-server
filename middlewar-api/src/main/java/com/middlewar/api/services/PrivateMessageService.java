package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.social.PrivateMessage;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
public interface PrivateMessageService extends CrudService<PrivateMessage, Integer> {
    PrivateMessage create(@NotNull Player author, @NotNull Player receiver, @NotEmpty String message);
}
