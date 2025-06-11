package services;

public class DeleteAccountErrorResponse {
    private final String op = "042";
    private String msg;

    public DeleteAccountErrorResponse(String message) {
        this.msg = message;
    }

    public String getMsg() { return msg; }
}