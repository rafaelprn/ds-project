package services;

// Resposta de erro para exclusão por admin (operação 092)
public class DeleteUserByAdminErrorResponse {
    private final String op = "092";
    private String msg;

    public DeleteUserByAdminErrorResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}