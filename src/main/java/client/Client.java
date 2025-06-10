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


public class Client {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 9345;
    private static String sessionToken = null;
    private static String loggedInUser = null;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);
        Gson gson = new Gson();

        LoginResponseHandler loginHandler = new LoginResponseHandler();
        RegistrationHandler registrationHandler = new RegistrationHandler();
        UserDataHandler userDataHandler = new UserDataHandler(); // NOVO

        try {
            socket = new DatagramSocket();
            InetAddress enderecoServidor = InetAddress.getByName(SERVER_ADDRESS);

            while (true) {
                // Menu principal de interação com o usuário
                System.out.println("\n============================");
                System.out.println("Bem-vindo! Escolha uma opção:");
                System.out.println("1. Login");
                System.out.println("2. Cadastro");
                System.out.println("3. Ver meus dados");
                System.out.println("4. Sair");
                System.out.println("============================");
                System.out.print("Opção: ");
                String choice = scanner.nextLine();

                if ("4".equals(choice)) {
                    System.out.println("Encerrando o cliente. Até logo!");
                    break;
                }

                String jsonRequest = null;

                // Roteamento da escolha do usuário
                if ("1".equals(choice)) {
                    // --- Lógica de Login ---
                    System.out.print("Digite o nome de usuário: ");
                    String user = scanner.nextLine();
                    System.out.print("Digite a senha: ");
                    String pass = scanner.nextLine();
                    LoginRequest loginRequest = new LoginRequest(user, pass);
                    jsonRequest = gson.toJson(loginRequest);

                    sendRequest(socket, jsonRequest, enderecoServidor, SERVER_PORT);
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
                } else {
                    System.out.println("Opção inválida. Por favor, tente novamente.");
                    continue;
                }

                // Envia a requisição e processa a resposta (para Cadastro ou Ver Dados)
                if (jsonRequest != null) {
                    sendRequest(socket, jsonRequest, enderecoServidor, SERVER_PORT);
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

                        continue; // Adicionado para voltar ao menu após a operação
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