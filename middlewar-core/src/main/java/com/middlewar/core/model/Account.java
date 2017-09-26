package com.middlewar.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.deserializer.AccountDeserializer;
import com.middlewar.core.enums.Lang;
import com.middlewar.core.serializer.AccountSerializer;
import com.middlewar.core.utils.Observable;
import com.middlewar.dto.AccountDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Entity
@Data
@NoArgsConstructor
@JsonSerialize(using = AccountSerializer.class)
@JsonDeserialize(using = AccountDeserializer.class)
public class Account extends Observable implements UserDetails {
    @Id
    @GeneratedValue
    private int id;
    private Lang lang;

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

    public Account(String username, String password, Collection<? extends GrantedAuthority> authorities, Lang lang, String token) {
        setUsername(username);
        setPassword(password);
        setLang(lang);
        setAuthorities(new HashSet<>(authorities));
        setPlayers(players == null ? new ArrayList<>() : players);
        setCurrentPlayer(currentPlayer);
        setToken(token);
    }

    public AccountDTO toDTO() {
        AccountDTO dto = new AccountDTO();
        dto.setId(this.getId());
        dto.setLang(this.getLang().getName());
        dto.setPlayers(this.getPlayers().stream().map(Player::getId).collect(Collectors.toList()));
        dto.setCurrentPlayer(this.getCurrentPlayer());
        dto.setToken(this.getToken());
        dto.setUsername(this.getUsername());
        return dto;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(new AuthorityComparator());
        Iterator var2 = authorities.iterator();

        while (var2.hasNext()) {
            GrantedAuthority grantedAuthority = (GrantedAuthority) var2.next();
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    public void addPlayer(Player player) {
        getPlayers().add(player);
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
