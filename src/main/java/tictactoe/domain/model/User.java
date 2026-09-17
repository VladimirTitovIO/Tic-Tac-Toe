package tictactoe.domain.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public class User  {
    private UUID id;
    private String login;
    private String password;
    private List<Role> roles;
    public UUID getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }

    public User() { }

    public User(UUID id, String login, String password, List<Role> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public enum Role implements GrantedAuthority {
        USER;

        @Override
        public String getAuthority() {
            return "ROLE_" + name();
        }
    }
}
