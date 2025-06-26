package services;

// Requisição para admin apagar um usuário (operação 090)
public class DeleteUserByAdminRequest {
    private final String op = "090";
    private String token;
    private String user;

    public DeleteUserByAdminRequest(String token, String user) {
        this.token = token;
        this.user = user;
    }

    // Getters
    public String getOp() { return op; }
    public String getToken() { return token; }
    public String getUser() { return user; }
}