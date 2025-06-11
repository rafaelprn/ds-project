package controllers;

import services.DeleteAccountRequest;
import services.DeleteAccountSuccessResponse;
import services.DeleteAccountErrorResponse;
import models.User;
import java.util.Map;

public class DeleteAccountController {

    public Object process(DeleteAccountRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // Validação de campos nulos
        if (request.getUser() == null || request.getToken() == null || request.getPass() == null) {
            return new DeleteAccountErrorResponse("Usuário, Token ou Senha nulos.");
        }

        String user = request.getUser();
        String token = request.getToken();
        String pass = request.getPass();

        // 1. Autenticação (Token): O token é válido e pertence ao usuário?
        if (!activeUsers.containsKey(token) || !activeUsers.get(token).equals(user)) {
            return new DeleteAccountErrorResponse("Token inválido ou não pertence ao usuário.");
        }

        // 2. Autenticação (Senha): A senha está correta?
        User userFromDb = userDatabase.get(user);
        if (userFromDb == null) {
            return new DeleteAccountErrorResponse("Usuário não existe.");
        }
        if (!pass.equals(userFromDb.getPassword())) {
            return new DeleteAccountErrorResponse("Senha errada.");
        }

        // 3. Sucesso: Apaga o usuário de todos os registros
        userDatabase.remove(user);      // Remove do "banco de dados" permanente
        activeUsers.remove(token);      // Remove da lista de usuários logados (logout forçado)

        System.out.println("[DeleteAccountController] Usuário " + user + " apagado com sucesso.");
        return new DeleteAccountSuccessResponse();
    }
}
