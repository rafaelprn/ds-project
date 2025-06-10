package services;

public class GetUserDataErrorResponse {
    private final String op = "007";
    private String msg;

    public GetUserDataErrorResponse(String message) {
        this.msg = message;
    }

    // Getters
    public String getOp() { return op; }
    public String getMsg() { return msg; }
}
