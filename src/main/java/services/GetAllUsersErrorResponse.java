package services;

// Resposta de erro (operação 112)
public class GetAllUsersErrorResponse {
    private final String op = "112";
    private String msg;

    public GetAllUsersErrorResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}