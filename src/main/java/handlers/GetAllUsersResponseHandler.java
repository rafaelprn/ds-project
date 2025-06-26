package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import services.GetAllUsersSuccessResponse;
import services.GetAllUsersErrorResponse;

public class GetAllUsersResponseHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        try {
            String opCode = JsonParser.parseString(jsonResponse)
                    .getAsJsonObject()
                    .get("op")
                    .getAsString();

            switch (opCode) {
                case "111":
                    GetAllUsersSuccessResponse success = gson.fromJson(jsonResponse, GetAllUsersSuccessResponse.class);
                    System.out.println("Lista de todos os usuarios cadastrados:");
                    if (success.getUserList() == null || success.getUserList().isEmpty()) {
                        System.out.println(" -> Nenhum usuario encontrado.");
                    } else {
                        for (String username : success.getUserList()) {
                            System.out.println(" -> " + username);
                        }
                    }
                    break;

                case "112":
                    GetAllUsersErrorResponse error = gson.fromJson(jsonResponse, GetAllUsersErrorResponse.class);
                    System.out.println("Erro ao buscar usuarios: " + error.getMsg());
                    break;

                default:
                    System.out.println("Erro: Resposta do servidor com operacao desconhecida: " + opCode);
                    break;
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Erro: A resposta do servidor esta mal formatada.");
        }
    }
}