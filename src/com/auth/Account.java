package com.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.Player;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class Account extends User
{
    @Id
    private String id;

    @DBRef
    @JsonView(View.Standard.class)
    private List<Player> players;

    @JsonView(View.Standard.class)
    private String token;

    public Account(String username, String password, Collection<GrantedAuthority> authorities, String id, List<Player> players, String token)
    {
        super(username, password, authorities);
        setId(id);
        setPlayers(players);
        setToken(token);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
