package controllers;

// Em 'controllers'
import services.LogoutRequest;
import services.LogoutSuccessResponse;
import services.LogoutErrorResponse;
import java.util.Map;

public class LogoutController {

    public Object process(LogoutRequest request, Map<String, String> activeUsers) {
        // Validação de campos nulos
        if (request.getUser() == null || request.getToken() == null) {
            return new LogoutErrorResponse("Token ou Usuário nulo.");
        }

        String token = request.getToken();
        String user = request.getUser();

        // Autenticação: O token é válido?
        if (!activeUsers.containsKey(token)) {
            return new LogoutErrorResponse("Token não existe ou é inválido.");
        }

        // Autorização: O token pertence ao usuário que está tentando fazer logout?
        if (!user.equals(activeUsers.get(token))) {
            return new LogoutErrorResponse("Token pertence a outro usuário.");
        }

        // Sucesso: Remove o token da lista de usuários ativos
        activeUsers.remove(token);
        System.out.println("[LogoutController] Usuário " + user + " deslogado com sucesso.");

        return new LogoutSuccessResponse();
    }
}
