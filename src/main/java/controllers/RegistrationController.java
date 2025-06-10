// Supondo que esteja no pacote 'controllers'
package controllers;
import services.RegistrationRequest;
import services.RegistrationSuccessResponse;
import services.RegistrationErrorResponse;
import models.User;
import java.util.Map;

public class RegistrationController {

    public Object processRegistration(RegistrationRequest request, Map<String, User> userDatabase) {
        // Validação de campos nulos
        if (request.getUser() == null || request.getNick() == null || request.getPass() == null) {
            return new RegistrationErrorResponse("Usuário, Nick ou Senha nulos.");
        }

        String user = request.getUser();
        String nick = request.getNick();
        String pass = request.getPass();

        // Validação de formato e tamanho
        if (user.length() < 6 || user.length() > 16 || !user.matches("[a-zA-Z0-9]+")) {
            return new RegistrationErrorResponse("Formato de Usuário errado.");
        }
        if (nick.length() < 6 || nick.length() > 16 || !nick.matches("[a-zA-Z0-9 ]+")) { // Permitindo espaços no nick
            return new RegistrationErrorResponse("Formato de Nick errado.");
        }
        if (pass.length() < 6 || pass.length() > 32 || !pass.matches("[a-zA-Z0-9]+")) {
            return new RegistrationErrorResponse("Formato de Senha errado.");
        }

        // Validação de unicidade (a mais importante)
        if (userDatabase.containsKey(user)) {
            return new RegistrationErrorResponse("Usuário já existe.");
        }

        // Sucesso! Adiciona o novo usuário ao "banco de dados"
        User newUser = new User(user, nick, pass);
        userDatabase.put(user, newUser);
        System.out.println("[RegistrationController] Usuário " + user + " cadastrado com sucesso.");

        return new RegistrationSuccessResponse();
    }
}