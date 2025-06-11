package controllers;

import services.UpdateProfileRequest;
import services.UpdateProfileSuccessResponse;
import services.UpdateProfileErrorResponse;
import models.User;
import java.util.Map;


public class UpdateProfileController {

    public Object process(UpdateProfileRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // Validação de campos obrigatórios nulos
        if (request.getUser() == null || request.getPass() == null || request.getToken() == null ||
                request.getNewNick() == null || request.getNewPass() == null) {
            return new UpdateProfileErrorResponse("Requisição com campos nulos.");
        }

        String token = request.getToken();
        String user = request.getUser();
        String pass = request.getPass();
        String newNick = request.getNewNick();
        String newPass = request.getNewPass();

        // 1. Autenticação (Token): O token é válido e pertence ao usuário?
        if (!activeUsers.containsKey(token) || !activeUsers.get(token).equals(user)) {
            return new UpdateProfileErrorResponse("Token inválido ou não pertence ao usuário.");
        }

        // 2. Autenticação (Senha): A senha atual está correta?
        User userFromDb = userDatabase.get(user);
        if (userFromDb == null) {
            return new UpdateProfileErrorResponse("Usuário não existe.");
        }
        if (!pass.equals(userFromDb.getPassword())) {
            return new UpdateProfileErrorResponse("Senha errada.");
        }

        // 3. Validação dos novos dados e atualização
        // Atualiza o Nickname se um novo foi fornecido
        if (!newNick.isEmpty()) {
            if (newNick.length() < 6 || newNick.length() > 16 || !newNick.matches("[a-zA-Z0-9 ]+")) {
                return new UpdateProfileErrorResponse("Formato de Novo Nick errado.");
            }
            userFromDb.setNickname(newNick);
            System.out.println("[UpdateProfileController] Nickname de " + user + " atualizado.");
        }

        // Atualiza a Senha se uma nova foi fornecida
        if (!newPass.isEmpty()) {
            if (newPass.length() < 6 || newPass.length() > 32 || !newPass.matches("[a-zA-Z0-9]+")) {
                return new UpdateProfileErrorResponse("Formato de Nova Senha errado.");
            }
            userFromDb.setPassword(newPass);
            System.out.println("[UpdateProfileController] Senha de " + user + " atualizada.");
        }

        // 4. Sucesso
        return new UpdateProfileSuccessResponse();
    }
}
