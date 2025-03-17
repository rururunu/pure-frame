package com.runu.web_server.config.security;

import com.runu.web_server.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode
public class SecurityUserDetail implements UserDetails {

    private User user;

    private Set<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null) {
            return authorities;
        } else {
            authorities = new HashSet<>();
        }
        user.getRoles().forEach(
                role -> authorities.add(
                        new SimpleGrantedAuthority(role.getRoleCode())
                )
        );
        user.getPowerCodes().forEach(power -> authorities.add(new SimpleGrantedAuthority(power))
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserAccount();
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
