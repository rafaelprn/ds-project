package services;

public class LoginRequest {
    private final String op = "000";
    private String user;
    private String pass;

    public LoginRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    // Getters para o servidor acessar os dados
    public String getOp() { return op; }
    public String getUser() { return user; }
    public String getPass() { return pass; }
}