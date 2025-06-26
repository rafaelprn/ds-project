package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.User;
import services.GetAllUsersRequest;
import services.GetAllUsersSuccessResponse;
import services.GetAllUsersErrorResponse;

public class AdminController {

    public Object processGetAllUsers(GetAllUsersRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // 1. Validação do token
        if (request.getToken() == null) {
            return new GetAllUsersErrorResponse("Token nulo.");
        }

        String token = request.getToken();

        // 2. Validação do formato e tipo do token
        if (!token.startsWith("a")) {
            return new GetAllUsersErrorResponse("Operacao nao permitida para este usuario.");
        }
        if (!activeUsers.containsKey(token)) {
            return new GetAllUsersErrorResponse("Token invalido ou sessao expirada.");
        }

        // 3. Sucesso: buscar e retornar a lista de usuários
        // Pega todos os nomes de usuário (chaves) do banco de dados
        List<String> allUsernames = new ArrayList<>(userDatabase.keySet());

        System.out.println("[AdminController] Admin " + activeUsers.get(token) + " solicitou a lista de todos os usuarios.");

        return new GetAllUsersSuccessResponse(allUsernames);
    }
}