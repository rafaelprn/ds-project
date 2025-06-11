// Server.java
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import controllers.LoginController;
import services.LoginRequest;

import controllers.RegistrationController;
import models.User;
import services.RegistrationRequest;

import services.RegistrationRequest;
import handlers.RegistrationHandler;

import controllers.UserDataController;
import handlers.UserDataHandler;
import services.GetUserDataRequest;

import services.LogoutRequest;
import controllers.LogoutController;

import controllers.UpdateProfileController;
import services.UpdateProfileRequest;

import controllers.DeleteAccountController;
import services.DeleteAccountRequest;

public class Server {

    private static final int PORT = 20000;
    private static final int BUFFER_SIZE = 1024;
    private static final Map<String, User> userDatabase = new HashMap<>();

    // Dependências que serão passadas para os controllers
    private static final Map<String, String> activeUsers = new HashMap<>();
    private static final AtomicInteger tokenCounter = new AtomicInteger(1);

    // Nossos controllers
    private static final LoginController loginController = new LoginController();
    private static final RegistrationController registrationController = new RegistrationController();
    private static final UserDataController userDataController = new UserDataController();
    private static final LogoutController logoutController = new LogoutController();
    private static final UpdateProfileController updateProfileController = new UpdateProfileController();
    private static final DeleteAccountController deleteAccountController = new DeleteAccountController();

    public static void main(String[] args) {
        DatagramSocket socket = null;
        Gson gson = new Gson();

        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Servidor UDP iniciado na porta " + PORT + "...");

            while (true) {
                // Recebe o pacote
                byte[] bufferRecepcao = new byte[BUFFER_SIZE];
                DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);
                socket.receive(pacoteRecepcao);

                String jsonRequest = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
                System.out.println("\n<-- Pacote recebido: " + jsonRequest);

                // Descobre a operação para rotear
                String opCode = getOpCodeFromJson(jsonRequest);
                Object responseObject = null;

                // Roteia para o Controller apropriado
                switch (opCode) {
                    case "000":
                        LoginRequest loginRequest = gson.fromJson(jsonRequest, LoginRequest.class);
                        // Garanta que o userDatabase está sendo passado aqui
                        responseObject = loginController.processLoginRequest(loginRequest, activeUsers, tokenCounter, userDatabase);
                        break;

                    case "005":
                        GetUserDataRequest userDataRequest = gson.fromJson(jsonRequest, GetUserDataRequest.class);
                        responseObject = userDataController.process(userDataRequest, activeUsers, userDatabase);
                        break;

                    case "010": // Requisição de Cadastro
                        RegistrationRequest regRequest = gson.fromJson(jsonRequest, RegistrationRequest.class);
                        responseObject = registrationController.processRegistration(regRequest, userDatabase);
                        break;

                    case "020":
                        LogoutRequest logoutRequest = gson.fromJson(jsonRequest, LogoutRequest.class);
                        responseObject = logoutController.process(logoutRequest, activeUsers);
                        break;

                    case "030":
                        UpdateProfileRequest updateRequest = gson.fromJson(jsonRequest, UpdateProfileRequest.class);
                        responseObject = updateProfileController.process(updateRequest, activeUsers, userDatabase);
                        break;

                    case "040":
                        DeleteAccountRequest deleteRequest = gson.fromJson(jsonRequest, DeleteAccountRequest.class);
                        responseObject = deleteAccountController.process(deleteRequest, activeUsers, userDatabase);
                        break;

                    default:
                        System.out.println("Operação desconhecida");
                }

                // Envia a resposta produzida pelo Controller
                if (responseObject != null) {
                    InetAddress ipCliente = pacoteRecepcao.getAddress();
                    int portaCliente = pacoteRecepcao.getPort();
                    String jsonResponse = gson.toJson(responseObject);
                    byte[] bufferEnvio = jsonResponse.getBytes();
                    DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, ipCliente, portaCliente);
                    socket.send(pacoteEnvio);
                    System.out.println("--> Resposta enviada: " + jsonResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }


     // Extrai o código de operação ("op") de uma string JSON sem desserializar o objeto inteiro.
    private static String getOpCodeFromJson(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            if (jsonObject.has("op")) {
                return jsonObject.get("op").getAsString();
            }
            return "";
        } catch (Exception e) {
            return ""; // Retorna vazio se o JSON for inválido ou não tiver a chave "op"
        }
    }
}