package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import services.UpdateUserByAdminSuccessResponse;
import services.UpdateUserByAdminErrorResponse;

public class UpdateUserByAdminResponseHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        try {
            String opCode = JsonParser.parseString(jsonResponse)
                    .getAsJsonObject()
                    .get("op")
                    .getAsString();

            switch (opCode) {
                case "081":
                    UpdateUserByAdminSuccessResponse success = gson.fromJson(jsonResponse, UpdateUserByAdminSuccessResponse.class);
                    System.out.println("Sucesso: " + success.getMsg());
                    break;

                case "082":
                    UpdateUserByAdminErrorResponse error = gson.fromJson(jsonResponse, UpdateUserByAdminErrorResponse.class);
                    System.out.println("Erro ao alterar cadastro: " + error.getMsg());
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