package services;

public class LoginErrorResponse {
    private final String op = "002";
    private String msg;

    public LoginErrorResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}