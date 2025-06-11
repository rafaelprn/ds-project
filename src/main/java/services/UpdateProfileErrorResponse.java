package services;

public class UpdateProfileErrorResponse {
    private final String op = "032";
    private String msg;

    public UpdateProfileErrorResponse(String message) {
        this.msg = message;
    }

    public String getMsg() { return msg; }
}