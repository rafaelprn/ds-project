package controllers;

import services.GetUserDataRequest;
import services.GetUserDataSuccessResponse;
import services.GetUserDataErrorResponse;
import models.User;
import java.util.Map;

public class UserDataController {

    public Object process(GetUserDataRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // Validação de campos nulos
        if (request.getUser() == null || request.getToken() == null) {
            return new GetUserDataErrorResponse("Usuário ou Token nulos.");
        }

        String token = request.getToken();
        String requestingUser = request.getUser();

        // 1. Autenticação: O token é válido e pertence ao usuário que faz o pedido?
        if (!activeUsers.containsKey(token) || !activeUsers.get(token).equals(requestingUser)) {
            return new GetUserDataErrorResponse("Usuário não autenticado ou token inválido.");
        }

        // 2. Recuperação de Dados: Busca os dados do usuário no "banco de dados"
        User userFromDb = userDatabase.get(requestingUser);
        if (userFromDb == null) {
            // Isso não deveria acontecer se o token é válido, mas é uma boa verificação
            return new GetUserDataErrorResponse("Usuário autenticado não encontrado na base de dados.");
        }

        // 3. Sucesso: Retorna os dados
        System.out.println("[UserDataController] Dados de " + requestingUser + " enviados.");
        return new GetUserDataSuccessResponse(userFromDb.getUsername(), userFromDb.getNickname());
    }
}
