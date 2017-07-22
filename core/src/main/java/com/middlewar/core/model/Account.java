package com.middlewar.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.serializer.AccountSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author LEBOC Philippe
 */
@Entity
@Data
@NoArgsConstructor
@JsonSerialize(using = AccountSerializer.class)
public class Account implements UserDetails
{
    @Id
    @GeneratedValue
    private String id;
    private Lang lang;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Player> players;
    private String currentPlayer;
    private String token;
    private String password;
    private String username;
    @ElementCollection
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public Account(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, Lang lang, List<Player> players, String currentPlayer, String token)
    {
        setId(id);
        setUsername(username);
        setPassword(password);
        setLang(lang);
        setAuthorities(Collections.unmodifiableSet(sortAuthorities(authorities)));
        setPlayers(players == null ? new ArrayList<>() : players);
        setCurrentPlayer(currentPlayer);
        setToken(token);
    }

    public void addPlayer(Player player){
        getPlayers().add(player);
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(new AuthorityComparator());
        Iterator var2 = authorities.iterator();

        while(var2.hasNext()) {
            GrantedAuthority grantedAuthority = (GrantedAuthority)var2.next();
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = 420L;

        private AuthorityComparator() {
        }

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            } else {
                return g1.getAuthority() == null ? 1 : g1.getAuthority().compareTo(g2.getAuthority());
            }
        }
    }
}
