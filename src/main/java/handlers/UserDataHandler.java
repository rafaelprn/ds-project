package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import services.GetUserDataSuccessResponse;
import services.GetUserDataErrorResponse;

public class UserDataHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        String opCode = getOpCodeFromJson(jsonResponse);
        switch (opCode) {
            case "006": // Sucesso
                try {
                    GetUserDataSuccessResponse success = gson.fromJson(jsonResponse, GetUserDataSuccessResponse.class);
                    System.out.println("Dados recebidos com sucesso:");
                    System.out.println("  Usuário: " + success.getUser());
                    System.out.println("  Nickname: " + success.getNick());
                } catch (JsonSyntaxException e) { /* ... */ }
                break;
            case "007": // Erro
                try {
                    GetUserDataErrorResponse error = gson.fromJson(jsonResponse, GetUserDataErrorResponse.class);
                    System.out.println("Erro ao buscar dados: " + error.getMsg());
                } catch (JsonSyntaxException e) { /* ... */ }
                break;
            default:
                // ...
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
