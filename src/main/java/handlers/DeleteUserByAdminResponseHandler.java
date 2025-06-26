package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import services.DeleteUserByAdminSuccessResponse;
import services.DeleteUserByAdminErrorResponse;

public class DeleteUserByAdminResponseHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        try {
            String opCode = JsonParser.parseString(jsonResponse)
                    .getAsJsonObject()
                    .get("op")
                    .getAsString();

            switch (opCode) {
                case "091":
                    DeleteUserByAdminSuccessResponse success = gson.fromJson(jsonResponse, DeleteUserByAdminSuccessResponse.class);
                    System.out.println("Sucesso: " + success.getMsg());
                    break;

                case "092":
                    DeleteUserByAdminErrorResponse error = gson.fromJson(jsonResponse, DeleteUserByAdminErrorResponse.class);
                    System.out.println("Erro ao apagar usuario: " + error.getMsg());
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