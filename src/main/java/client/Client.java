// Client.java (Modificado para TCP)
package client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import services.LoginRequest;
import services.RegistrationRequest;
import handlers.LoginResponseHandler;
import handlers.RegistrationHandler;
import handlers.UserDataHandler;
import services.GetUserDataRequest;
import handlers.LogoutHandler;
import services.LogoutRequest;
import handlers.UpdateProfileHandler;
import services.UpdateProfileRequest;
import handlers.DeleteAccountHandler;
import services.DeleteAccountRequest;

import handlers.GetAllUsersResponseHandler;
import services.GetAllUsersRequest;
import handlers.UpdateUserByAdminResponseHandler;
import services.UpdateUserByAdminRequest;

public class Client {

    private static String sessionToken = null;
    private static String loggedInUser = null;

    // Canais de comunicação TCP
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o endereço IP do servidor (pressione Enter para usar '127.0.0.1'): ");
        String serverAddressStr = scanner.nextLine();
        if (serverAddressStr.isEmpty()) {
            serverAddressStr = "127.0.0.1"; // Valor padrão
        }

        int serverPort = 0;
        while (true) {
            try {
                System.out.print("Digite a porta do servidor: ");
                String portInput = scanner.nextLine();
                serverPort = Integer.parseInt(portInput);
                break; // Sai do loop se a porta for um número válido
            } catch (NumberFormatException e) {
                System.out.println("Erro: Entrada inválida. Por favor, digite um número de porta válido.");
            }
        }

        // Conectar ao servidor TCP UMA VEZ no início
        try {
            socket = new Socket(serverAddressStr, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true); // true para auto-flush
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado ao servidor em " + serverAddressStr + ":" + serverPort);

            LoginResponseHandler loginHandler = new LoginResponseHandler();
            RegistrationHandler registrationHandler = new RegistrationHandler();
            UserDataHandler userDataHandler = new UserDataHandler();
            LogoutHandler logoutHandler = new LogoutHandler();
            UpdateProfileHandler updateHandler = new UpdateProfileHandler();
            DeleteAccountHandler deleteAccountHandler = new DeleteAccountHandler();


            GetAllUsersResponseHandler getAllUsersHandler = new GetAllUsersResponseHandler();
            UpdateUserByAdminResponseHandler updateUserByAdminHandler = new UpdateUserByAdminResponseHandler();

            while (true) {
                // Menu principal de interação com o usuário
                System.out.println("\n============================");
                System.out.println("Bem-vindo! Escolha uma opção:");
                System.out.println("1. Login");
                System.out.println("2. Cadastro");
                System.out.println("3. Ver meus dados");
                System.out.println("4. Logout");
                System.out.println("5. Alterar Cadastro");
                System.out.println("6. Apagar Minha Conta");
                System.out.println("7. Sair");

                if (sessionToken != null && sessionToken.startsWith("a")) {
                    System.out.println("--- Opcoes de Administrador ---");
                    System.out.println("8. Listar todos os usuarios");
                    System.out.println("9. Alterar cadastro de um usuario");
                }

                System.out.println("============================");
                System.out.print("Opção: ");
                String choice = scanner.nextLine();

                if ("7".equals(choice)) {
                    System.out.println("Encerrando o cliente. Até logo!");
                    break;
                }

                String jsonRequest = null;
                boolean shouldWaitForResponse = true;

                // Roteamento da escolha do usuário
                if ("1".equals(choice)) {
                    // --- Lógica de Login ---
                    System.out.print("Digite o nome de usuário: ");
                    String user = scanner.nextLine();
                    System.out.print("Digite a senha: ");
                    String pass = scanner.nextLine();
                    LoginRequest loginRequest = new LoginRequest(user, pass);
                    jsonRequest = gson.toJson(loginRequest);

                    // Envia a requisição e espera a resposta
                    out.println(jsonRequest);
                    String jsonResponse = in.readLine();

                    System.out.println("\n--- Resultado do Login ---");
                    System.out.println("<-- Resposta recebida: " + jsonResponse);
                    loginHandler.handle(jsonResponse, user);
                    continue; // Volta ao início do loop do menu

                } else if ("2".equals(choice)) {
                    // --- Lógica de Cadastro ---
                    System.out.println("\n--- Sistema de Cadastro ---");
                    System.out.print("Digite o nome de usuário desejado [6-16 caracteres]: ");
                    String user = scanner.nextLine();
                    System.out.print("Digite seu nome/apelido [6-16 caracteres]: ");
                    String nick = scanner.nextLine();
                    System.out.print("Digite a senha desejada [6-32 caracteres]: ");
                    String pass = scanner.nextLine();
                    RegistrationRequest regRequest = new RegistrationRequest(user, nick, pass);
                    jsonRequest = gson.toJson(regRequest);

                } else if ("3".equals(choice)) {
                    if (sessionToken == null) {
                        System.out.println("Erro: Você precisa estar logado para ver seus dados.");
                        shouldWaitForResponse = false;
                    } else {
                        GetUserDataRequest userDataRequest = new GetUserDataRequest(sessionToken, loggedInUser);
                        jsonRequest = gson.toJson(userDataRequest);
                    }
                } else if ("4".equals(choice)) {
                    if (sessionToken == null) {
                        System.out.println("Erro: Você precisa estar logado para fazer logout.");
                        shouldWaitForResponse = false;
                    } else {
                        LogoutRequest logoutRequest = new LogoutRequest(loggedInUser, sessionToken);
                        jsonRequest = gson.toJson(logoutRequest);
                    }
                } else if ("5".equals(choice)) {
                    if (sessionToken == null) {
                        System.out.println("Erro: Você precisa estar logado para esta operação.");
                        shouldWaitForResponse = false;
                    } else {
                        System.out.println("\n--- Alteração de Cadastro ---");
                        System.out.print("Para confirmar, digite sua senha ATUAL: ");
                        String currentPass = scanner.nextLine();
                        System.out.print("Digite o novo nick: ");
                        String newNick = scanner.nextLine();
                        System.out.print("Digite a nova senha: ");
                        String newPass = scanner.nextLine();
                        UpdateProfileRequest updateRequest = new UpdateProfileRequest(loggedInUser, currentPass, newNick, newPass, sessionToken);
                        jsonRequest = gson.toJson(updateRequest);
                    }
                } else if ("6".equals(choice)) {
                    if (sessionToken == null) {
                        System.out.println("Erro: Você precisa estar logado para apagar sua conta.");
                        shouldWaitForResponse = false;
                    } else {
                        System.out.println("\n--- APAGAR CONTA ---");
                        System.out.println("ATENÇÃO: Esta ação é irreversível.");
                        System.out.print("Para confirmar, digite sua senha: ");
                        String currentPass = scanner.nextLine();
                        DeleteAccountRequest deleteRequest = new DeleteAccountRequest(loggedInUser, sessionToken, currentPass);
                        jsonRequest = gson.toJson(deleteRequest);
                    }
                } else if ("8".equals(choice) && sessionToken != null && sessionToken.startsWith("a")) {
                    GetAllUsersRequest request = new GetAllUsersRequest(sessionToken);
                    jsonRequest = gson.toJson(request);
                } else if ("9".equals(choice) && sessionToken != null && sessionToken.startsWith("a")) {
                    System.out.println("\n--- Alterar Cadastro de Usuario (Admin) ---");
                    System.out.print("Digite o nome de usuario a ser alterado: ");
                    String targetUser = scanner.nextLine();

                    System.out.print("Digite o novo nick (deixe em branco para nao alterar): ");
                    String newNick = scanner.nextLine();

                    System.out.print("Digite a nova senha (deixe em branco para nao alterar): ");
                    String newPass = scanner.nextLine();

                    UpdateUserByAdminRequest request = new UpdateUserByAdminRequest(sessionToken, targetUser, newNick, newPass);
                    jsonRequest = gson.toJson(request);
                    // -------------------------

                }
                else {
                    System.out.println("Opção inválida.");
                    shouldWaitForResponse = false;
                }

                // Envia a requisição e processa a resposta
                if (shouldWaitForResponse && jsonRequest != null) {
                    System.out.println("\n--> Requisição enviada: " + jsonRequest);
                    out.println(jsonRequest);
                    String jsonResponse = in.readLine();
                    System.out.println("<-- Resposta recebida: " + jsonResponse);

                    if ("2".equals(choice)) {
                        System.out.println("\n--- Resultado do Cadastro ---");
                        registrationHandler.handle(jsonResponse);
                    } else if ("3".equals(choice)) {
                        System.out.println("\n--- Resultado da Consulta ---");
                        userDataHandler.handle(jsonResponse);
                    } else if ("4".equals(choice)) {
                        System.out.println("\n--- Resultado do Logout ---");
                        logoutHandler.handle(jsonResponse);
                    } else if ("5".equals(choice)) {
                        System.out.println("\n--- Resultado da Alteração ---");
                        updateHandler.handle(jsonResponse);
                    } else if ("6".equals(choice)) {
                        System.out.println("\n--- Resultado da Exclusão ---");
                        deleteAccountHandler.handle(jsonResponse);
                    } else if ("8".equals(choice)) {
                        System.out.println("\n--- Resultado da Listagem de Usuarios ---");
                        getAllUsersHandler.handle(jsonResponse);
                    } else if ("9".equals(choice)) {
                        System.out.println("\n--- Resultado da Alteracao de Cadastro ---");
                        updateUserByAdminHandler.handle(jsonResponse);
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Erro: Host desconhecido. Verifique o endereço do servidor.");
        } catch (IOException e) {
            System.err.println("Erro de comunicação com o servidor: A conexão foi perdida ou o servidor foi encerrado.");
        } finally {
            // Fecha todos os recursos no final
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                scanner.close();
                System.out.println("Recursos liberados.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Os métodos de sessão permanecem os mesmos
    public static void setSession(String token, String username) {
        sessionToken = token;
        loggedInUser = username;
    }

    public static void clearSession() {
        sessionToken = null;
        loggedInUser = null;
    }
}