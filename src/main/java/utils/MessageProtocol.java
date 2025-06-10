// Pacote apropriado, ex: com.seuprojeto.protocolo
// public class MensagemProtocolo { // Removido para simplificar a execução em um único arquivo de exemplo

// Definição da classe dentro do cliente/servidor para o exemplo,
// mas idealmente seria em um arquivo separado e compartilhado.
class MessageProtocol {
    String op;
    String token;
    String title;
    String subject;
    String msg;

    // Construtor (opcional, mas útil)
    public MessageProtocol(String op, String token, String title, String subject, String msg) {
        this.op = op;
        this.token = token;
        this.title = title;
        this.subject = subject;
        this.msg = msg;
    }

    // Getters e Setters (opcional, GSON pode acessá-los diretamente se forem públicos,
    // mas é boa prática tê-los)
    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    @Override
    public String toString() {
        return "MensagemProtocolo{" +
                "op='" + op + '\'' +
                ", token='" + token + '\'' +
                ", title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}