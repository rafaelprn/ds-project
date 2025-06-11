package services;

public class LogoutRequest {
    private final String op = "020";
    private String user;
    private String token;

    public LogoutRequest(String user, String token) {
        this.user = user;
        this.token = token;
    }

    // Getters para o Controller
    public String getOp() { return op; }
    public String getUser() { return user; }
    public String getToken() { return token; }
}