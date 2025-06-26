package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import models.User;

// Importe todos os seus controllers e services necessários
import controllers.LoginController;
import controllers.RegistrationController;
import controllers.UserDataController;
import controllers.LogoutController;
import controllers.UpdateProfileController;
import controllers.DeleteAccountController;
import services.LoginRequest;
import services.RegistrationRequest;
import services.LogoutRequest;
import services.LoginErrorResponse;
import services.GetUserDataRequest;
import services.DeleteAccountRequest;
import services.UpdateProfileRequest;


// A classe ClientHandler implementa Runnable para poder ser executada em uma thread
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Gson gson;
    private final Map<String, User> userDatabase;
    private final Map<String, String> activeUsers;
    private final AtomicInteger tokenCounter;

    // Controllers
    private final LoginController loginController;
    private final RegistrationController registrationController;
    private final UserDataController userDataController;
    private final LogoutController logoutController;
    private final UpdateProfileController updateProfileController;
    private final DeleteAccountController deleteAccountController;

    private PrintWriter out;
    private BufferedReader in;

    // O construtor recebe tudo o que precisa do servidor principal
    public ClientHandler(Socket socket, Gson gson, Map<String, User> userDatabase, Map<String, String> activeUsers, AtomicInteger tokenCounter, LoginController loginController, RegistrationController registrationController, UserDataController userDataController, LogoutController logoutController, UpdateProfileController updateProfileController, DeleteAccountController deleteAccountController) {
        this.clientSocket = socket;
        this.gson = gson;
        this.userDatabase = userDatabase;
        this.activeUsers = activeUsers;
        this.tokenCounter = tokenCounter;
        this.loginController = loginController;
        this.registrationController = registrationController;
        this.userDataController = userDataController;
        this.logoutController = logoutController;
        this.updateProfileController = updateProfileController;
        this.deleteAccountController = deleteAccountController;
    }

    @Override
    public void run() {
        try {
            // Prepara os canais de comunicação (streams)
            out = new PrintWriter(clientSocket.getOutputStream(), true); // true para auto-flush
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String jsonRequest;
            // O loop continua enquanto o cliente estiver conectado e enviando dados
            while ((jsonRequest = in.readLine()) != null) {
                System.out.println("\n<-- Mensagem recebida de " + clientSocket.getInetAddress().getHostAddress() + ": " + jsonRequest);

                String opCode = getOpCodeFromJson(jsonRequest);
                Object responseObject = null;

                // A lógica de roteamento é a mesma de antes
                switch (opCode) {
                    case "000": // Login
                        LoginRequest loginRequest = gson.fromJson(jsonRequest, LoginRequest.class);
                        responseObject = loginController.processLoginRequest(loginRequest, activeUsers, tokenCounter, userDatabase);
                        break;
                    case "005": // Get User Data
                        GetUserDataRequest userDataRequest = gson.fromJson(jsonRequest, GetUserDataRequest.class);
                        responseObject = userDataController.process(userDataRequest, activeUsers, userDatabase);
                        break;
                    case "010": // Registration
                        RegistrationRequest regRequest = gson.fromJson(jsonRequest, RegistrationRequest.class);
                        responseObject = registrationController.processRegistration(regRequest, userDatabase);
                        break;
                    case "020": // Logout
                        LogoutRequest logoutRequest = gson.fromJson(jsonRequest, LogoutRequest.class);
                        responseObject = logoutController.process(logoutRequest, activeUsers);
                        break;
                    case "030": // Update Profile
                        UpdateProfileRequest updateRequest = gson.fromJson(jsonRequest, UpdateProfileRequest.class);
                        responseObject = updateProfileController.process(updateRequest, activeUsers, userDatabase);
                        break;
                    case "040": // Delete Account
                        DeleteAccountRequest deleteRequest = gson.fromJson(jsonRequest, DeleteAccountRequest.class);
                        responseObject = deleteAccountController.process(deleteRequest, activeUsers, userDatabase);
                        break;
                    default:
                        System.out.println("Operação desconhecida: " + opCode);
                        responseObject = new LoginErrorResponse("Operação desconhecida: " + opCode);
                }

                if (responseObject != null) {
                    String jsonResponse = gson.toJson(responseObject);
                    // Envia a resposta de volta para o cliente através do seu stream dedicado
                    out.println(jsonResponse);
                    System.out.println("--> Resposta enviada para " + clientSocket.getInetAddress().getHostAddress() + ": " + jsonResponse);
                }
            }
        } catch (IOException e) {
            System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
            // Aqui você pode adicionar lógica para limpar o usuário caso ele estivesse logado
        } finally {
            try {
                // Fecha os recursos do cliente
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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