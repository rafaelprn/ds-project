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

    // Getters necessários
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
}
