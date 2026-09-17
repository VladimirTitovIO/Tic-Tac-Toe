package tictactoe.web.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import tictactoe.domain.model.User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class JwtAuthentication implements Authentication {

    private final UUID userId;
    private final List<User.Role> roles;
    private boolean authenticated;

    public JwtAuthentication(UUID userId, List<User.Role> roles, boolean authenticated) {
        this.userId = userId;
        this.roles = roles;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return userId.toString();
    }
}
