package services;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Resposta de sucesso com a lista de usuários (operação 111)
public class GetAllUsersSuccessResponse {
    private final String op = "111";

    @SerializedName("user_list") // Garante que o nome no JSON seja "user_list"
    private List<String> userList;

    public GetAllUsersSuccessResponse(List<String> userList) {
        this.userList = userList;
    }

    // Getters
    public String getOp() { return op; }
    public List<String> getUserList() { return userList; }
}