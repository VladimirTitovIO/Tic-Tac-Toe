package tictactoe.datasource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public UserEntity() { }

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
