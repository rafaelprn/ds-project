package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import models.User;

import server.ClientHandler;

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

import controllers.AdminController;


public class Server {

    private static final int BUFFER_SIZE = 1024;

    // Dependências que serão passadas para os controllers
    private static final Map<String, User> userDatabase = new ConcurrentHashMap<>();
    private static final Map<String, String> activeUsers = new ConcurrentHashMap<>();
    private static final AtomicInteger tokenCounter = new AtomicInteger(1);

    // controllers
    private static final LoginController loginController = new LoginController();
    private static final RegistrationController registrationController = new RegistrationController();
    private static final UserDataController userDataController = new UserDataController();
    private static final LogoutController logoutController = new LogoutController();
    private static final UpdateProfileController updateProfileController = new UpdateProfileController();
    private static final DeleteAccountController deleteAccountController = new DeleteAccountController();

    private static final AdminController adminController = new AdminController(); //admin

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        addAdminUser();

        Scanner configScanner = new Scanner(System.in);
        int port = 0;

        while (true) {
            try {
                System.out.print("Digite a porta em que o servidor TCP deve rodar: ");
                String portInput = configScanner.nextLine();
                port = Integer.parseInt(portInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite um número de porta válido.");
            }
        }
        configScanner.close();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor TCP iniciado na porta " + port + ". Aguardando clientes...");

            while (true) {
                // O método accept() bloqueia até que um cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Criar e iniciar uma nova thread para cada cliente
                // Passamos todas as dependências necessárias para o handler do cliente
                ClientHandler clientHandler = new ClientHandler(
                        clientSocket,
                        gson,
                        userDatabase,
                        activeUsers,
                        tokenCounter,
                        loginController,
                        registrationController,
                        userDataController,
                        logoutController,
                        updateProfileController,
                        deleteAccountController,
                        adminController
                );

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addAdminUser() {
        String adminUser = "admin123";
        // Só adiciona o admin se ele não existir, para evitar sobreposição
        if (!userDatabase.containsKey(adminUser)) {
            User admin = new User(adminUser, "admin123", "admin123");
            userDatabase.put(adminUser, admin);
            System.out.println("Usuário administrador 'admin123' criado e pronto para uso.");
        }
    }
}