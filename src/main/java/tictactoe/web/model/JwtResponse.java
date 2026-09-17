package tictactoe.web.model;

public class JwtResponse {
    private String type;
    private String accessToken;
    private String refreshToken;

    public JwtResponse(String type, String accessToken, String refreshToken) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setType(String type) { this.type = type; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getType() { return type; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
