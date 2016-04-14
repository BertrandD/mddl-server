package com.commons.SpringConfig;

import com.auth.AccountService;
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
	AccountService accountService;

	@Autowired
	AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter;

	@Autowired
	public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception {
		// The authentication provider below uses MongoDB to store SHA1 hashed passwords
		// To see how to configure users for the example below, please see the README file
		auth
			.userDetailsService(accountService)
			.passwordEncoder(new BCryptPasswordEncoder());
			//.passwordEncoder(new ShaPasswordEncoder());
	}

	@Override
	public void configure( WebSecurity web ) throws Exception
	{
		// This is here to ensure that the static content (JavaScript, CSS, etc)
		// is accessible from the login page without authentication
		web
			.ignoring()
			.antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(authenticationTokenProcessingFilter, BasicAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.and()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/login**").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/login**").permitAll()
				.antMatchers(HttpMethod.POST, "/login", "/logout", "/", "/register").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/login", "/logout", "/", "/register").permitAll()
				.anyRequest().authenticated()
			.and()
			.httpBasic().and().csrf().disable();
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
