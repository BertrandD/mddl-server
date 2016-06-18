package com.config.spring;

import com.auth.Account;
import com.auth.AccountService;
import com.util.Utils;
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

/**
 * @author Bertrand
 */
@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationManager authManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked")
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // FIXME ? Là je pars du principe que le token est dans le header X-auth-token, est-ce qu'on fait ça ou on le met dans les cookies ? ça change pas grand chose pour le back, à voir pour le front
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
                }
            } else {
                Utils.println("Invalid token !");
            }
        }
        // continue thru the filter chain
        chain.doFilter(request, response);
    }
}