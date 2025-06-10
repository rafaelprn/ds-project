// LoginResponseHandler.java (CORRIGIDO)
package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import services.LoginRequest;
import services.LoginSuccessResponse;
import services.LoginErrorResponse;
import client.Client;

public class LoginResponseHandler {

    private final Gson gson = new Gson();

    public void handle(String jsonResponse, String username) {
        // Descobrir a operação
        String opCode = getOpCodeFromJson(jsonResponse);

        // Direciona com base na operação
        switch (opCode) {
            case "001": // Resposta de Sucesso
                try {
                    LoginSuccessResponse success = gson.fromJson(jsonResponse, LoginSuccessResponse.class);
                    System.out.println("Login bem-sucedido!");

                    Client.setSession(success.getToken(), username);
                    System.out.println("Sessão iniciada. Seu token é: " + success.getToken());
                    // Guardando token para futuras requisições
                } catch (JsonSyntaxException e) {
                    System.out.println("Erro: A mensagem de sucesso (001) está mal formatada.");
                }
                break;

            case "002": // Resposta de Erro
                try {
                    LoginErrorResponse error = gson.fromJson(jsonResponse, LoginErrorResponse.class);
                    System.out.println("Falha no login: " + error.getMsg());
                } catch (JsonSyntaxException e) {
                    System.out.println("Erro: A mensagem de erro (002) está mal formatada.");
                }
                break;

            default: // Qualquer outra resposta
                System.out.println("Erro: Resposta do servidor com operação desconhecida: '" + opCode + "'");
                break;
        }
    }

    private String getOpCodeFromJson(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            if (jsonObject.has("op")) {
                return jsonObject.get("op").getAsString();
            }
            return ""; // Retorna vazio se não tiver a chave "op"
        } catch (Exception e) {
            return ""; // Retorna vazio se o JSON for inválido
        }
    }
}