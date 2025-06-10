// Supondo que esteja no pacote 'services'
package services;

public class RegistrationRequest {
    private final String op = "010";
    private String user;
    private String nick;
    private String pass;

    public RegistrationRequest(String user, String nick, String pass) {
        this.user = user;
        this.nick = nick;
        this.pass = pass;
    }

    // Getters para o Controller acessar
    public String getOp() { return op; }
    public String getUser() { return user; }
    public String getNick() { return nick; }
    public String getPass() { return pass; }
}