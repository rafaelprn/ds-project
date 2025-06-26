package services;

// Resposta de sucesso para exclusão por admin (operação 091)
public class DeleteUserByAdminSuccessResponse {
    private final String op = "091";
    private String msg;

    public DeleteUserByAdminSuccessResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}