package com.middlewar.api.config.spring;

import com.middlewar.core.model.Account;
import com.middlewar.api.auth.AccountService;
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
 * @author Bertrand
 */
@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private final Logger logger = Logger.getLogger(AuthenticationTokenProcessingFilter.class.getName());

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    AccountService accountService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked")
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        // FIXME ? Là je pars du principe que le token est dans le header X-auth-token, est-ce qu'on fait ça ou on le met dans les cookies ? ça change pas grand chose pour le back, à voir pour le front
        // TODO ! Il faut que ce token soit dans les cookies pour éviter les failles XSS
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
                    logger.info("Account is null");
                }
            } else {
                logger.info("Invalid token !");
            }
        }
        // continue thru the filter chain
        chain.doFilter(request, response);
    }
}