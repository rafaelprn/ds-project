package services;

// Resposta de sucesso para alteração por admin (operação 081)
public class UpdateUserByAdminSuccessResponse {
    private final String op = "081";
    private String msg;

    public UpdateUserByAdminSuccessResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}