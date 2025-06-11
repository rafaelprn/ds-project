package services;

public class DeleteAccountRequest {
    private final String op = "040";
    private String user;
    private String token;
    private String pass;

    public DeleteAccountRequest(String user, String token, String pass) {
        this.user = user;
        this.token = token;
        this.pass = pass;
    }

    // Getters para o Controller
    public String getUser() { return user; }
    public String getToken() { return token; }
    public String getPass() { return pass; }
}
