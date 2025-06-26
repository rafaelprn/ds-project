package services;

import com.google.gson.annotations.SerializedName;

// Requisição para admin alterar dados de usuário (operação 080)
public class UpdateUserByAdminRequest {
    private final String op = "080";
    private String token;
    private String user;

    @SerializedName("new_nick")
    private String newNick;

    @SerializedName("new_pass")
    private String newPass;

    public UpdateUserByAdminRequest(String token, String user, String newNick, String newPass) {
        this.token = token;
        this.user = user;
        this.newNick = newNick;
        this.newPass = newPass;
    }

    // Getters
    public String getOp() { return op; }
    public String getToken() { return token; }
    public String getUser() { return user; }
    public String getNewNick() { return newNick; }
    public String getNewPass() { return newPass; }
}