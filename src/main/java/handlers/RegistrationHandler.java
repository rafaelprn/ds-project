// Supondo que esteja no pacote 'handlers'
package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import services.RegistrationErrorResponse;

public class RegistrationHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        String opCode = getOpCodeFromJson(jsonResponse);

        switch (opCode) {
            case "011":
                System.out.println("Cadastro realizado com sucesso!");
                break;
            case "012":
                try {
                    RegistrationErrorResponse error = gson.fromJson(jsonResponse, RegistrationErrorResponse.class);
                    System.out.println("Erro no cadastro: " + error.getMsg());
                } catch (JsonSyntaxException e) {
                    System.out.println("Erro: A mensagem de erro (012) está mal formatada.");
                }
                break;
            default:
                System.out.println("Erro: Resposta de cadastro desconhecida: '" + opCode + "'");
                break;
        }
    }

    private String getOpCodeFromJson(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            return jsonObject.has("op") ? jsonObject.get("op").getAsString() : "";
        } catch (Exception e) {
            return "";
        }
    }
}