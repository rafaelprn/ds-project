package services;

// Resposta de erro para alteração por admin (operação 082)
public class UpdateUserByAdminErrorResponse {
    private final String op = "082";
    private String msg;

    public UpdateUserByAdminErrorResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}