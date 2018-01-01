package com.middlewar.core.model;

import com.middlewar.core.annotations.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "accounts")
@NoArgsConstructor
public class Account implements UserDetails {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "account", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Player> players = new ArrayList<>();

    private int currentPlayerId;

    @NotEmpty
    @Column(unique = true)
    @Size(min = 4, max = 16)
    private String username;

    @NotEmpty
    @Password
    private String password;

    @Fetch(FetchMode.JOIN)
    @ElementCollection
    private Set<GrantedAuthority> authorities = new HashSet<>();

    public Account(String username, String password, Set<GrantedAuthority> authorities) {
        setUsername(username);
        setPassword(password);
        setAuthorities(authorities);
        setPlayers(emptyList());
        setCurrentPlayerId(-1);
    }

    public void addPlayer(@NotNull Player player) {
        if(players != null && !players.contains(player))
            players.add(player);
        else
            log.warn("Cannot add Player " + player.getName() + " to account because of null or already added");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return singletonList(() -> "USER");
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
