package com.middlewar.api.util.response;

import com.middlewar.api.exceptions.ApiException;
import com.middlewar.api.exceptions.UnauthorizedException;
import com.middlewar.api.manager.AccountManager;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author Bertrand
 */
@Service
public class ControllerManagerWrapper {

    @Autowired
    AccountManager accountManager;

    public Response wrap(Callable<Object> callable) {
        Account pAccount;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            pAccount = new Account();
            pAccount.setLang(Lang.EN);
        } else {
            pAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        try {
            Object o = callable.call();
            if (o instanceof Response) {
                return (Response) o;
            }
            return new Response<>(o);
        } catch (ApiException e) {
            Response response = new Response<>(JsonResponseType.ERROR, pAccount.getLang(), e.getMessage());
            if (e instanceof UnauthorizedException) {
                response.setStatus(JsonResponseType.UNAUTHORIZED);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(JsonResponseType.ERROR, pAccount.getLang(), SystemMessageId.INTERNAL_ERROR);
        }
    }
}
