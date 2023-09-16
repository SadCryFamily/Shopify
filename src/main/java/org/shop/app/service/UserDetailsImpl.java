package org.shop.app.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.shop.app.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Getter
public class UserDetailsImpl implements UserDetails {

    private String clientName;

    private String clientPassword;

    private boolean isDeleted;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String clientName, String clientPassword, boolean isDeleted, Collection<? extends GrantedAuthority> authorities) {
        this.clientName = clientName;
        this.clientPassword = clientPassword;
        this.isDeleted = isDeleted;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Client client) {

        List<GrantedAuthority> authorities = client.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                client.getClientName(),
                client.getClientPassword(),
                client.isDeleted(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return clientPassword;
    }

    @Override
    public String getUsername() {
        return clientName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isDeleted;
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
