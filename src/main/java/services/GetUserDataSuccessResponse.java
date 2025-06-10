package services;

public class GetUserDataSuccessResponse {
    private final String op = "006";
    private String user;
    private String nick;

    public GetUserDataSuccessResponse(String user, String nick) {
        this.user = user;
        this.nick = nick;
    }

    // Getters para o Handler usar
    public String getOp() { return op; }
    public String getUser() { return user; }
    public String getNick() { return nick; }
}