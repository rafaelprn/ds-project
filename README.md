# Sistema Cliente-Servidor com Protocolo JSON via UDP
Este é um projeto de um sistema cliente-servidor desenvolvido em Java. A comunicação entre o cliente e o servidor é realizada através de sockets UDP, e as mensagens são trocadas utilizando um protocolo customizado baseado em objetos JSON.
A arquitetura do servidor utiliza um padrão de Handler/Controller para organizar a lógica de negócio de forma limpa e escalável.

## Funcionalidades Implementadas 
* **Gerenciamento de Usuários:**
    * Cadastro de novos usuários.
    * Login e Logout com gerenciamento de sessão via token.
    * Alteração de dados cadastrais (nickname e senha).
    * Consulta dos próprios dados.
    * Exclusão de conta.

## Como Rodar
* **Pré-Requisito:** A biblioteca [Google GSON](https://github.com/google/gson) para manipulação de JSON.

### 1. Preparação do Ambiente
1.  **Clone ou Baixe o Projeto:**
2.  **Adicione a Biblioteca GSON:**
    Certifique-se de que a biblioteca GSON está adicionada como uma dependência do seu projeto. Se você estiver usando Maven ou Gradle, pode adicioná-la ao seu arquivo de configuração. Caso contrário, você pode baixar o arquivo `.jar` e adicioná-lo manualmente ao classpath do projeto na sua IDE.

### 2. Executando o Servidor
O servidor precisa ser iniciado primeiro, pois ele ficará aguardando as conexões dos clientes.

1.  Execute o arquivo `Server.java`.
2.  O terminal irá solicitar a porta em que o servidor deve rodar. Digite um número de porta (ex: `20000`) e pressione Enter.
3.  O servidor agora está ativo e pronto para receber e processar pacotes.

### 3. Executando o Cliente
Você pode rodar uma ou várias instâncias do cliente para se conectar ao servidor.
1.  Execute o arquivo `Client.java`.
2.  O terminal irá primeiro solicitar as informações de conexão do servidor:
    * **Endereço IP:** Digite o endereço IP da máquina onde o servidor está rodando. Se for no mesmo computador, você pode utilizar `localhost`
    * **Porta:** Digite a mesma porta que você configurou no servidor (ex: `20000`).
3.  Após a configuração, o menu principal de funcionalidades será exibido. Siga as instruções no terminal para interagir com o sistema (fazer login, cadastrar, etc.).
