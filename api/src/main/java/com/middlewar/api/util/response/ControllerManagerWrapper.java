package com.middlewar.api.util.response;

import com.middlewar.api.exceptions.ApiException;
import com.middlewar.core.model.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author Bertrand
 */
@Service
public class ControllerManagerWrapper {
    public Response wrap(Callable<Object> callable) {
        Account pAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Object o = callable.call();
            if (o instanceof Response) {
                return (Response)o;
            }
            return new Response(o);
        } catch (ApiException e) {
            return new Response(JsonResponseType.ERROR, pAccount.getLang(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(JsonResponseType.ERROR, pAccount.getLang(), SystemMessageId.INTERNAL_ERROR);
        }
    }
}
