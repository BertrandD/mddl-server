package com.middlewar.boot.config;

import com.middlewar.api.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author LEBOC Philippe
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter;

    @Autowired
    private AccountService accountService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(accountService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // This is here to ensure that the static content (JavaScript, CSS, etc)
        // is accessible from the login page without authentication
        web
            .ignoring()
            .antMatchers("/v2/api-docs", "**/configuration/ui/**", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/console/*")
            .antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(authenticationTokenProcessingFilter, BasicAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.GET, "/").permitAll()
            .antMatchers(HttpMethod.GET, "/login**").permitAll()
            .antMatchers(HttpMethod.POST, "/login", "/logout", "/", "/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic().and().csrf().disable();

        // H2 need
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configAuthBuilder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(accountService);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return accountService;
    }
}

