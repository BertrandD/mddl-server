package com.middlewar.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Account implements UserDetails {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "account", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Player> players = new ArrayList<>();

    private int currentPlayer;

    private String token;

    private String password;

    private String username;

    @Fetch(FetchMode.JOIN)
    @ElementCollection
    private Set<GrantedAuthority> authorities = new HashSet<>();

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private boolean deleted;

    public Account(String username, String password, Collection<? extends GrantedAuthority> authorities, String token) {
        setUsername(username);
        setPassword(password);
        setAuthorities(new HashSet<>(authorities));
        setPlayers(players == null ? new ArrayList<>() : players);
        setCurrentPlayer(currentPlayer);
        setToken(token);
    }

    public void addPlayer(Player player) {
        getPlayers().add(player);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
