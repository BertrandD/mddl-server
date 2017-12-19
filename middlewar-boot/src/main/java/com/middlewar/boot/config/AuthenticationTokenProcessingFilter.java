package com.middlewar.boot.config;

import com.middlewar.api.services.AccountService;
import com.middlewar.core.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * TODO: TO BE DELETED
 * @author Bertrand
 */
@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private final Logger log = Logger.getLogger(AuthenticationTokenProcessingFilter.class.getName());

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AccountService accountService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked") final HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("X-auth-token");

        if (token != null) {
            // validate the token
            if (accountService.validateToken(token)) {
                // determine the user based on the (already validated) token
                Account account = accountService.getUserFromToken(token);
                if (account != null) {
                    // build an Authentication object with the user's info
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                    // set the authentication into the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(authentication));
                } else {
                    log.info("Account is null");
                }
            } else {
                log.info("Invalid token !");
            }
        }
        // continue thru the filter chain
        chain.doFilter(request, response);
    }
}