package tictactoe.web.model;

import org.springframework.stereotype.Component;

@Component
public class RefreshJwtRequest {
    private String refreshToken;

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getRefreshToken() { return refreshToken; }
}
