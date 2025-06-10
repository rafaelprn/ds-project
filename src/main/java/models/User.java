// Supondo que esteja no pacote 'models'
package models;

public class User {
    private String username;
    private String nickname;
    private String password;

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    // LoginController pode verificar a senha
    public String getPassword() {
        return this.password;
    }
}