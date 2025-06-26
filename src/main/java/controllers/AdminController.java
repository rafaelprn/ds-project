package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.User;

import services.GetAllUsersRequest;
import services.GetAllUsersSuccessResponse;
import services.GetAllUsersErrorResponse;

import services.UpdateUserByAdminRequest;
import services.UpdateUserByAdminSuccessResponse;
import services.UpdateUserByAdminErrorResponse;

import services.DeleteUserByAdminRequest;
import services.DeleteUserByAdminSuccessResponse;
import services.DeleteUserByAdminErrorResponse;

public class AdminController {

    public Object processGetAllUsers(GetAllUsersRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // 1. Validação do token
        if (request.getToken() == null) {
            return new GetAllUsersErrorResponse("Token nulo.");
        }

        String token = request.getToken();

        // 2. Validação do formato e tipo do token
        if (!token.startsWith("a")) {
            return new GetAllUsersErrorResponse("Operacao nao permitida para este usuario.");
        }
        if (!activeUsers.containsKey(token)) {
            return new GetAllUsersErrorResponse("Token invalido ou sessao expirada.");
        }

        // 3. Sucesso: buscar e retornar a lista de usuários
        // Pega todos os nomes de usuário (chaves) do banco de dados
        List<String> allUsernames = new ArrayList<>(userDatabase.keySet());

        System.out.println("[AdminController] Admin " + activeUsers.get(token) + " solicitou a lista de todos os usuarios.");

        return new GetAllUsersSuccessResponse(allUsernames);
    }

    public Object processUpdateUser(UpdateUserByAdminRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // 1. Validação de campos obrigatórios nulos
        if (request.getToken() == null || request.getUser() == null) {
            return new UpdateUserByAdminErrorResponse("Token ou usuario-alvo nulo.");
        }

        String token = request.getToken();
        String targetUser = request.getUser();

        // 2. Validação do token do admin
        if (!token.startsWith("a")) {
            return new UpdateUserByAdminErrorResponse("Operacao nao permitida para este usuario.");
        }
        if (!activeUsers.containsKey(token)) {
            return new UpdateUserByAdminErrorResponse("Token invalido ou sessao expirada.");
        }

        // 3. Validação do usuário-alvo
        if (targetUser.equals("admin123")) {
            return new UpdateUserByAdminErrorResponse("Usuario admin nao pode ser alterado.");
        }
        User userFromDb = userDatabase.get(targetUser);
        if (userFromDb == null) {
            return new UpdateUserByAdminErrorResponse("Usuario-alvo nao existe.");
        }

        // 4. Validação e atualização dos novos dados (nick e senha)
        String newNick = request.getNewNick();
        String newPass = request.getNewPass();

        // Atualiza o Nickname se um novo foi fornecido
        if (newNick != null && !newNick.isEmpty()) {
            if (newNick.length() < 6 || newNick.length() > 16 || !newNick.matches("[a-zA-Z0-9 ]+")) {
                return new UpdateUserByAdminErrorResponse("Formato de Novo Nick errado.");
            }
            userFromDb.setNickname(newNick);
            System.out.println("[AdminController] Nickname de " + targetUser + " atualizado pelo admin " + activeUsers.get(token));
        }

        // Atualiza a Senha se uma nova foi fornecida
        if (newPass != null && !newPass.isEmpty()) {
            if (newPass.length() < 6 || newPass.length() > 32 || !newPass.matches("[a-zA-Z0-9]+")) {
                return new UpdateUserByAdminErrorResponse("Formato de Nova Senha errado.");
            }
            userFromDb.setPassword(newPass);
            System.out.println("[AdminController] Senha de " + targetUser + " atualizada pelo admin " + activeUsers.get(token));
        }

        // 5. Sucesso
        return new UpdateUserByAdminSuccessResponse("Cadastro de " + targetUser + " alterado com sucesso.");
    }

    public Object processDeleteUser(DeleteUserByAdminRequest request, Map<String, String> activeUsers, Map<String, User> userDatabase) {
        // 1. Validação de campos obrigatórios nulos
        if (request.getToken() == null || request.getUser() == null) {
            return new DeleteUserByAdminErrorResponse("Token ou usuario-alvo nulo.");
        }

        String token = request.getToken();
        String targetUser = request.getUser();

        // 2. Validação do token do admin
        if (!token.startsWith("a")) {
            return new DeleteUserByAdminErrorResponse("Operacao nao permitida para este usuario.");
        }
        if (!activeUsers.containsKey(token)) {
            return new DeleteUserByAdminErrorResponse("Token invalido ou sessao expirada.");
        }

        // 3. Validação do usuário-alvo
        if (targetUser.equals("admin123")) {
            return new DeleteUserByAdminErrorResponse("Usuario admin nao pode ser deletado.");
        }
        if (!userDatabase.containsKey(targetUser)) {
            return new DeleteUserByAdminErrorResponse("Usuario-alvo nao existe.");
        }

        // 4. Sucesso: Apaga o usuário de todos os registros
        userDatabase.remove(targetUser); // Remove do "banco de dados" permanente

        // Adicional: remove o usuário da lista de ativos se ele estiver logado
        // Isso efetivamente força o logout do usuário deletado
        activeUsers.values().removeIf(username -> username.equals(targetUser));

        System.out.println("[AdminController] Usuario " + targetUser + " apagado pelo admin " + activeUsers.get(token));

        return new DeleteUserByAdminSuccessResponse("Usuario " + targetUser + " apagado com sucesso.");
    }
}