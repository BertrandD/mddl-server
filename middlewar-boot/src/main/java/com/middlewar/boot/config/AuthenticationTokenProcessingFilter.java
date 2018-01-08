package com.middlewar.boot.config;

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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked") final HttpServletRequest httpRequest = (HttpServletRequest) request;

        // continue thru the filter chain
        chain.doFilter(request, response);
    }
}