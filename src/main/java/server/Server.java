import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // DatagramSocket na porta 9876
            socket = new DatagramSocket(9876);
            System.out.println("Servidor UDP iniciado na porta 9876...");

            byte[] bufferRecepcao = new byte[1024];

            while (true) { // loop para continuar recebendo mensagens
                // DatagramPacket para receber dados
                DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);

                // aguarda o recebimento de pacotes
                socket.receive(pacoteRecepcao);

                // processa os dados recebidos
                String mensagemRecebida = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
                InetAddress enderecoIPCliente = pacoteRecepcao.getAddress();
                int portaCliente = pacoteRecepcao.getPort();

                System.out.println("Mensagem recebida de " + enderecoIPCliente.getHostAddress() + ":" + portaCliente + ": " + mensagemRecebida);

                // prepara e envia uma resposta ao cliente
                String mensagemResposta = "Mensagem recebida pelo servidor: \"" + mensagemRecebida + "\"";
                byte[] bufferEnvio = mensagemResposta.getBytes();

                DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, enderecoIPCliente, portaCliente);
                socket.send(pacoteEnvio);
                System.out.println("Resposta enviada para o cliente.");

                // limpa o buffer para a próxima recepção
                bufferRecepcao = new byte[1024];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}