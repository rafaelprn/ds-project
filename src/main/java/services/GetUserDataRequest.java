package services;

public class GetUserDataRequest {
    private final String op = "005";
    private String token;
    private String user;

    public GetUserDataRequest(String token, String user) {
        this.token = token;
        this.user = user;
    }

    // Getters
    public String getOp() { return op; }
    public String getToken() { return token; }
    public String getUser() { return user; }
}