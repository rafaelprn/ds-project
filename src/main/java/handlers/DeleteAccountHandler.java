package handlers;

import client.Client;
import com.google.gson.Gson;
import services.DeleteAccountErrorResponse;

public class DeleteAccountHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        // A resposta de sucesso é simples, podemos fazer uma verificação direta
        if (jsonResponse.contains("\"op\":\"041\"")) {
            System.out.println("Sua conta foi apagada com sucesso.");
            // Limpa os dados da sessão no cliente, pois o usuário não existe mais
            Client.clearSession();
        } else {
            // Se não for sucesso, trata como erro
            try {
                DeleteAccountErrorResponse error = gson.fromJson(jsonResponse, DeleteAccountErrorResponse.class);
                System.out.println("Erro ao apagar conta: " + error.getMsg());
            } catch (Exception e) {
                System.out.println("Erro: Resposta do servidor desconhecida.");
            }
        }
    }
}