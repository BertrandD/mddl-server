package com.middlewar.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.serializer.AccountSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@JsonSerialize(using = AccountSerializer.class)
public class Account extends User
{
    @Id
    private String id;
    private Lang lang;
    private List<String> players;
    private String currentPlayer;
    private String token;

    public Account(String username, String password, Collection<GrantedAuthority> authorities, String id, Lang lang, List<String> players, String currentPlayer, String token)
    {
        super(username, password, authorities);
        setId(id);
        setLang(lang);
        setPlayers(players == null ? new ArrayList<>() : players);
        setCurrentPlayer(currentPlayer);
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

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(String player){
        getPlayers().add(player);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    public String getPassword() {
        return super.getPassword();
    }

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
