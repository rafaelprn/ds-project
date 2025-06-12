// Ajuste o nome do pacote conforme a sua estrutura
package client;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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


public class Client {

    private static String sessionToken = null;
    private static String loggedInUser = null;

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

        DatagramSocket socket = null;
        Gson gson = new Gson();

        LoginResponseHandler loginHandler = new LoginResponseHandler();
        RegistrationHandler registrationHandler = new RegistrationHandler();
        UserDataHandler userDataHandler = new UserDataHandler();
        LogoutHandler logoutHandler = new LogoutHandler();
        UpdateProfileHandler updateHandler = new UpdateProfileHandler();
        DeleteAccountHandler deleteAccountHandler = new DeleteAccountHandler();

        try {
            socket = new DatagramSocket();
            InetAddress enderecoServidor = InetAddress.getByName(serverAddressStr);

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

                    sendRequest(socket, jsonRequest, enderecoServidor, serverPort);
                    String jsonResponse = receiveResponse(socket);

                    System.out.println("\n--- Resultado do Login ---");

                    loginHandler.handle(jsonResponse, user);

                    continue;

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
                        continue;
                    }
                    GetUserDataRequest userDataRequest = new GetUserDataRequest(sessionToken, loggedInUser);
                    jsonRequest = gson.toJson(userDataRequest);
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
                } else {
                    System.out.println("Opção inválida.");
                    shouldWaitForResponse = false;
                }

                // Envia a requisição e processa a resposta (para Cadastro ou Ver Dados)
                if (shouldWaitForResponse && jsonRequest != null) {
                    sendRequest(socket, jsonRequest, enderecoServidor, serverPort);
                    String jsonResponse = receiveResponse(socket);

                    if ("2".equals(choice)) {
                        System.out.println("\n--- Resultado do Cadastro ---");
                        registrationHandler.handle(jsonResponse);
                    } else if ("3".equals(choice)) {
                        // Primeiro, verificamos se o usuário está logado (se o token existe)
                        if (sessionToken == null) {
                            System.out.println("Erro: Você precisa estar logado para ver seus dados.");
                            continue; // Volta para o menu
                        }
                        GetUserDataRequest userDataRequest = new GetUserDataRequest(sessionToken, loggedInUser);
                        jsonRequest = gson.toJson(userDataRequest);

                        System.out.println("\n--- Resultado da Consulta ---");
                        userDataHandler.handle(jsonResponse);

                        continue;
                    } else if ("4".equals(choice)) {
                        System.out.println("\n--- Resultado do Logout ---");
                        logoutHandler.handle(jsonResponse);
                    } else if ("5".equals(choice)) {
                        System.out.println("\n--- Resultado da Alteração ---");
                        updateHandler.handle(jsonResponse);
                    } else if ("6".equals(choice)) {
                        System.out.println("\n--- Resultado da Exclusão ---");
                        deleteAccountHandler.handle(jsonResponse);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erro de comunicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            scanner.close();
            System.out.println("Recursos liberados.");
        }
    }

    public static void setSession(String token, String username) {
        sessionToken = token;
        loggedInUser = username;
    }

    public static void clearSession() {
        sessionToken = null;
        loggedInUser = null;
    }

    private static void sendRequest(DatagramSocket socket, String jsonRequest, InetAddress serverAddress, int serverPort) throws IOException {
        byte[] bufferEnvio = jsonRequest.getBytes();
        DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, serverAddress, serverPort);
        socket.send(pacoteEnvio);
        System.out.println("\n--> Requisição enviada: " + jsonRequest);
    }

    private static String receiveResponse(DatagramSocket socket) throws IOException {
        byte[] bufferRecepcao = new byte[1024];
        DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);
        socket.receive(pacoteRecepcao);
        String jsonResponse = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
        System.out.println("<-- Resposta recebida: " + jsonResponse);
        return jsonResponse;
    }
}