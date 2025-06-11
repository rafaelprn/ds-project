package services;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    private final String op = "030";
    private String user;
    private String pass;

    @SerializedName("new_nick")
    private String newNick;

    @SerializedName("new_pass")
    private String newPass;

    private String token;

    public UpdateProfileRequest(String user, String pass, String newNick, String newPass, String token) {
        this.user = user;
        this.pass = pass;
        this.newNick = newNick;
        this.newPass = newPass;
        this.token = token;
    }

    // Getters para o Controller
    public String getUser() { return user; }
    public String getPass() { return pass; }
    public String getNewNick() { return newNick; }
    public String getNewPass() { return newPass; }
    public String getToken() { return token; }
}
