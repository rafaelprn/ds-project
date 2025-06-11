package handlers;

import client.Client; // Importa a classe Client para poder chamar o clearSession
import com.google.gson.Gson;
import services.LogoutErrorResponse;

public class LogoutHandler {
    private final Gson gson = new Gson();

    public void handle(String jsonResponse) {
        // Para o logout, a resposta de sucesso é muito simples ("op":"021")
        // Podemos fazer uma verificação direta
        if (jsonResponse.contains("\"op\":\"021\"")) {
            System.out.println("Logout realizado com sucesso!");
            // Limpa os dados da sessão no cliente
            Client.clearSession();
        } else {
            // Se não for sucesso, trata como erro
            try {
                LogoutErrorResponse error = gson.fromJson(jsonResponse, LogoutErrorResponse.class);
                System.out.println("Erro no logout: " + error.getMsg());
            } catch (Exception e) {
                System.out.println("Erro: Resposta de logout desconhecida.");
            }
        }
    }
}