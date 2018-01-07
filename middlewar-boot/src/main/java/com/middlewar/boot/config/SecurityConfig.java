package com.middlewar.boot.config;

import com.middlewar.api.bean.auth.CustomAuthenticationEntryPoint;
import com.middlewar.api.bean.auth.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author LEBOC Philippe
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService accountService;

    @Autowired
    private CustomAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenProcessingFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(accountService)
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
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
            .httpBasic().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
            .and()
            .authorizeRequests()
            .antMatchers(OPTIONS, "/**").permitAll()

            // TODO: uncomment these global rules
            //.antMatchers("/public/**").permitAll()
            //.antMatchers("/api/**").authenticated()
            //.antMatchers("/admin/**").hasAnyRole("ADMIN")
            // TODO: remove that permitAll()
            .antMatchers(GET, "/**").permitAll()
            .antMatchers(POST, "/**").permitAll()
            .antMatchers(PUT, "/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().cacheControl().disable()
            .and()
            .addFilterBefore(authenticationTokenProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        // H2 need
        http.headers().frameOptions().disable();
    }
}

