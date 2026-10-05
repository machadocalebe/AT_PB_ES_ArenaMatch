# Guia Rápido de Execução - ArenaMatch

Este é um guia passo a passo simplificado para iniciar o projeto ArenaMatch do zero, sem precisar de conhecimentos profundos em programação ou infraestrutura.

## Pré-requisitos
Para rodar este projeto no seu computador, você precisa ter instalado apenas 2 programas:
1. **Docker Desktop**: É o motor que vai rodar os "contêineres" isolados do nosso sistema. Baixe e instale em: [docker.com](https://www.docker.com/products/docker-desktop)
2. **Git**: Para baixar o código para a sua máquina (ou você pode apenas baixar o ZIP do projeto no GitHub).

---

## 🚀 Passo a Passo para Iniciar o Projeto

### Passo 1: Iniciar o Docker Desktop
Abra o programa **Docker Desktop** no seu computador e aguarde até que o ícone na barra de tarefas fique verde (indicando que o motor está rodando).

### Passo 2: Abrir o Terminal na Pasta do Projeto
1. Encontre a pasta onde os arquivos do projeto `projetoArenaMatch-tp1` estão salvos.
2. Abra um terminal de linha de comando **dentro desta pasta**. (No Windows, você pode abrir a pasta, clicar na barra de endereços, digitar `cmd` ou `powershell` e apertar Enter).

### Passo 3: Executar a Mágica (Docker Compose)
Com o terminal aberto na raiz do projeto (onde está o arquivo `docker-compose.yml`), digite o seguinte comando e aperte Enter:

```bash
docker compose up --build -d
```

> **O que isso faz?** O comando vai baixar as ferramentas necessárias da internet (como o RabbitMQ), compilar o nosso código Java (Backend) e Javascript (Frontend), colocá-los dentro de "caixas" isoladas (contêineres) e ligar todas essas caixas umas nas outras automaticamente.

### Passo 4: Acessar o Sistema
Aguarde cerca de 1 a 2 minutos para o sistema inteiro ligar. Em seguida, você pode testar o projeto acessando os seguintes endereços pelo seu navegador:

- **1. Acessar a Interface do Sistema (React Frontend):**
  🔗 [http://localhost](http://localhost)
  > *Aqui você verá a tela de Login. Como é sua primeira vez, clique em **Registrar**, crie um usuário rapidamente e faça o login para acessar o painel principal com tema Pastel!*

- **2. Acessar a API do Backend (Healthcheck Actuator):**
  🔗 [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
  > *Aqui você verá um texto parecido com `{"status":"UP"}` confirmando que o cérebro do sistema e o banco de dados estão saudáveis.*

- **3. Acessar o Painel do RabbitMQ (Mensageria):**
  🔗 [http://localhost:15672](http://localhost:15672)
  > *Faça login com usuário: `guest` e senha: `guest`. Aqui você pode ver os gráficos das filas de eventos em tempo real quando cria ou cancela reservas na interface!*

---

## 🛑 Como Desligar o Sistema

Quando terminar de testar, volte ao terminal e digite:

```bash
docker compose down
```

Isso irá parar e destruir os contêineres de forma limpa, não deixando restos no seu computador.
