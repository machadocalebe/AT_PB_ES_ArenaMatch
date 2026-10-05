# Evidências e Escopo do Projeto (TP5)

O projeto ArenaMatch comporta, sim, **TODO** o escopo exigido na transição de monólito para microsserviços. 

**Resumo do Escopo Entregue:**
*   **Design / Arquitetura:** Transição para um padrão desacoplado orientado a domínio (DDD).
*   **Segurança (JWT):** Implementação do Spring Security para autenticação robusta via tokens JWT e criptografia de senhas (BCrypt).
*   **APIs RESTful:** Uso de Spring Boot para expor os serviços e Spring Data JPA para persistência em memória (H2).
*   **Event-Driven (Mensageria):** Implementação de microsserviços orientados a eventos utilizando **RabbitMQ** (O cadastro de Avaliações é totalmente assíncrono via `AvaliacaoWorker`).
*   **Frontend Avançado:** Interface React responsiva com tema "Pastel" minimalista (Apple-style), integração com Axios Interceptors (para injetar JWT) e `react-big-calendar` com funcionalidade Drag and Drop avançada (ajuste de fuso-horário via date-fns).
*   **Conteinerização:** `Dockerfile` multi-stage implementados para Backend e Frontend.
*   **Orquestração Local & Produção:** `docker-compose.yml` conectando toda a infraestrutura, além de arquivos `.yaml` do **Kubernetes** para escalonamento automático (HPA).
*   **Monitoramento:** Integração com **Spring Actuator** (`/actuator/health`) configurado nos liveness probes com exceção de segurança apropriada.
*   **CI/CD Automatizado:** Pipeline criada no **GitHub Actions** que faz testes lint (Frontend), testes unitários com Mockito (Backend), build e push das imagens para o **Docker Hub**.

---

## 📸 Pasta de Evidências (`/evidencias/`)

Como solicitado, criei uma pasta chamada `evidencias/` na raiz deste projeto. Para o professor avaliar seu trabalho com clareza (junto com o seu vídeo), **você deve tirar capturas de tela (Prints) e colar os arquivos de imagem dentro da pasta `evidencias/`**.

### Quais capturas você deve fazer e salvar lá dentro:

1. **`evidencia-01-github-actions-verde.png`**
   * **Onde tirar:** Acesse a aba "Actions" do repositório no seu GitHub e tire um print mostrando a pipeline "CI/CD Pipeline - ArenaMatch" concluída com o checkmark verde (✅).

2. **`evidencia-02-docker-hub-imagens.png`**
   * **Onde tirar:** Acesse a sua conta do Docker Hub (hub.docker.com), vá nos seus Repositórios e tire um print mostrando que as imagens `arenamatch-backend` e `arenamatch-frontend` subiram há pouco tempo e estão online.

3. **`evidencia-03-rabbitmq-eventos.png`**
   * **Onde tirar:** Com o projeto rodando, faça uma avaliação de uma quadra na interface, abra o [http://localhost:15672](http://localhost:15672) (usuário e senha `guest`), vá na aba "Queues" e clique em `avaliacao.queue`. Tire um print mostrando os gráficos com picos de mensagens (Consumer ack).

4. **`evidencia-04-monitoramento-actuator.png`**
   * **Onde tirar:** Acesse [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) e tire um print da tela preta/branca mostrando o JSON `{"status":"UP"...}` para comprovar o monitoramento funcionando.

> 💡 **Nota:** Após colar essas 4 imagens na pasta `evidencias`, basta fazer o Commit e enviar tudo para o professor!
