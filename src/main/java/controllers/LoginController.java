// Supondo que esteja no pacote 'controllers'
package controllers;

import com.google.gson.Gson;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import models.User; // Precisamos importar a classe User
import services.LoginRequest;
import services.LoginSuccessResponse;
import services.LoginErrorResponse;


public class LoginController {
    public Object processLoginRequest(LoginRequest request, Map<String, String> activeUsers, AtomicInteger tokenCounter, Map<String, User> userDatabase) {
        // 1. Validação de formato (sem alterações)
        if (request.getUser() == null || request.getPass() == null) {
            return new LoginErrorResponse("Usuário ou Senha nulos.");
        }
        if (request.getUser().length() < 6 || request.getUser().length() > 16 || !request.getUser().matches("[a-zA-Z0-9]+")) {
            return new LoginErrorResponse("Formato de Usuário errado.");
        }
        if (request.getPass().length() < 6 || request.getPass().length() > 32 || !request.getPass().matches("[a-zA-Z0-9]+")) {
            return new LoginErrorResponse("Formato de Senha errado.");
        }

        // 2. Autenticação (base de dados)
        String username = request.getUser();
        User userFromDb = userDatabase.get(username); // Tenta buscar o usuário no "banco de dados"

        // Verifica se o usuário existe
        if (userFromDb == null) {
            return new LoginErrorResponse("Usuário não existe.");
        }

        // Verifica se a senha está correta
        if (!request.getPass().equals(userFromDb.getPassword())) {
            return new LoginErrorResponse("Senha errada.");
        }

        // Verifica se o usuário já está logado
        if (activeUsers.containsValue(request.getUser())) {
            return new LoginErrorResponse("Usuário já está logado.");
        }

        // Se sucesso gerar e armazenar token
        String token = "c" + tokenCounter.getAndIncrement();
        activeUsers.put(token, request.getUser());
        System.out.println("[LoginController] Usuário " + request.getUser() + " logado com token: " + token);

        return new LoginSuccessResponse(token);
    }
}