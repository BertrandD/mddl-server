package com.middlewar.controllers;

import com.middlewar.api.bean.auth.JwtAuthenticationRequest;
import com.middlewar.api.bean.auth.JwtAuthenticationResponse;
import com.middlewar.api.services.AccountService;
import com.middlewar.api.util.JwtUtil;
import com.middlewar.api.util.response.Response;
import com.middlewar.core.model.Account;
import com.middlewar.dto.AccountDto;
import com.middlewar.request.AccountCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Leboc Philippe.
 */
@RestController
@RequestMapping(value = "/public/auth", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/login", method = POST)
    public Response login(@RequestBody JwtAuthenticationRequest request) {
        // Perform the security
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // add authentication to the context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = accountService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        return new Response(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "/register", method = POST)
    public Response register(@RequestBody AccountCreationRequest request) {
        final Account account = accountService.create(request.getUsername(), request.getPassword());
        return new Response(new AccountDto(account, jwtUtil.generateToken(account)));
    }
}
