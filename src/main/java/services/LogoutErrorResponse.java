package services;

public class LogoutErrorResponse {
    private final String op = "022";
    private String msg;

    public LogoutErrorResponse(String message) {
        this.msg = message;
    }

    public String getMsg() { return msg; }
}
