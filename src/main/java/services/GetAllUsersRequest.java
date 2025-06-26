package services;

// Requisição para buscar todos os usuários (operação 110)
public class GetAllUsersRequest {
    private final String op = "110";
    private String token;

    public GetAllUsersRequest(String token) {
        this.token = token;
    }

    // Getters
    public String getOp() { return op; }
    public String getToken() { return token; }
}