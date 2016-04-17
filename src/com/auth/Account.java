package com.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.enums.Lang;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Account extends User
{
    @Id
    private String id;

    @JsonView(View.Standard.class)
    private Lang lang;

    @JsonIgnore
    private List<String> players;

    @JsonIgnore
    private String currentPlayuer;

    @JsonView(View.Standard.class)
    private String token;

    public Account(String username, String password, Collection<GrantedAuthority> authorities, String id, Lang lang, List<String> players, String currentPlayuer, String token)
    {
        super(username, password, authorities);
        setId(id);
        setLang(lang);
        setPlayers(players == null ? new ArrayList<>() : players);
        setCurrentPlayuer(currentPlayuer);
        setToken(token);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    @JsonIgnore
    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(String player){
        getPlayers().add(player);
    }

    @JsonIgnore
    public String getCurrentPlayuer() {
        return currentPlayuer;
    }

    public void setCurrentPlayuer(String currentPlayuer) {
        this.currentPlayuer = currentPlayuer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonView(View.Standard.class)
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    public String getPassword() {
        return super.getPassword();
    }

    @JsonView(View.Standard.class)
    public String getUsername() {
        return super.getUsername();
    }

    public boolean isEnabled() {
        return super.isEnabled();
    }

    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
