import Client.Client;
import Server.Server;

public class Main {
    public static void main(String[] args) {
        Client client1 = new Client();
        Server server1 = new Server();
        server1.sendMessage(client1.message);
    }
}
