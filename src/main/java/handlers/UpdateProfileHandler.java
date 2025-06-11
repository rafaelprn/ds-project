package handlers;

import com.google.gson.Gson;
import services.UpdateProfileErrorResponse;

public class UpdateProfileHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        if (jsonResponse.contains("\"op\":\"031\"")) {
            System.out.println("Cadastro alterado com sucesso!");
        } else {
            try {
                UpdateProfileErrorResponse error = gson.fromJson(jsonResponse, UpdateProfileErrorResponse.class);
                System.out.println("Erro ao alterar cadastro: " + error.getMsg());
            } catch (Exception e) {
                System.out.println("Erro: Resposta de alteração de cadastro desconhecida.");
            }
        }
    }
}