package tictactoe.domain.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String login;
    private String password;

    public UUID getId() {
        return id;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public User() { }

    public User(UUID id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }
}
