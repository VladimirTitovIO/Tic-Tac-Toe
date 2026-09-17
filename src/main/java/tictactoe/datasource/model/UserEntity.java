package tictactoe.datasource.model;

import jakarta.persistence.*;
import tictactoe.domain.model.User.Role;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public UserEntity() { }

    public void setRoles(List<Role> roles) { this.roles = roles; }
    public List<Role> getRoles() { return roles; }
    public UUID getId() {
        return id;
    }
    public String getLogin() { return login; }
    public String getPassword() { return password; }

    public UserEntity(UUID id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }
}
