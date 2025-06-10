package services;

public class LoginSuccessResponse {
    private final String op = "001";
    private String token;

    public LoginSuccessResponse(String token) {
        this.token = token;
    }

    // Getters
    public String getOp() { return op; }
    public String getToken() { return token; }
}