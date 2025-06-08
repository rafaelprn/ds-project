import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new DatagramSocket();

            // obter ip server (localhost para testar)
            InetAddress enderecoServidor = InetAddress.getByName("localhost");
            int portaServidor = 9876;

            System.out.print("Digite a mensagem para enviar ao servidor: ");
            String mensagemEnvio = scanner.nextLine();
            byte[] bufferEnvio = mensagemEnvio.getBytes();

            // DatagramPacket para enviar dados
            DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, enderecoServidor, portaServidor);

            // envia o pacote
            socket.send(pacoteEnvio);
            System.out.println("Mensagem enviada para o servidor: " + mensagemEnvio);

            // prepara para receber uma resposta do servidor
            byte[] bufferRecepcao = new byte[1024];
            DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);

            socket.receive(pacoteRecepcao); // aguarda a resposta

            String mensagemResposta = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
            System.out.println("Resposta do servidor: " + mensagemResposta);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            scanner.close();
        }
    }
}