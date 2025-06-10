// Supondo que esteja no pacote 'services'
package services;

public class RegistrationErrorResponse {
    private final String op = "012";
    private String msg;

    public RegistrationErrorResponse(String message) {
        this.msg = message;
    }

    public String getOp() { return op; }
    public String getMsg() { return msg; }
}