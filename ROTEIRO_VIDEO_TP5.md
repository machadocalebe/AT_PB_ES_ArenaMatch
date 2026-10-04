# Roteiro de Gravação - Vídeo Final ArenaMatch (AT)
**Tempo Máximo:** 2 minutos.
**Foco do Vídeo:** Mostrar o sistema rodando na prática pelo Frontend, provar a comunicação com o Backend, demonstrar a autenticação e comprovar o funcionamento da Mensageria (RabbitMQ).

> **Dica de Preparação:** Deixe o Frontend aberto de um lado da tela e o painel do RabbitMQ (`http://localhost:15672`) ou o terminal rodando os logs do Docker do outro lado. 

---

## 🎬 0:00 - 0:30 | Introdução, Evolução e Autenticação
**O que mostrar na tela:** A tela inicial de Login do Frontend. Faça o login na conta.
**O que falar:**
*"Olá! Este é o ArenaMatch. O projeto começou como um monólito simples e, através de um processo de desenvolvimento ágil, nós o evoluímos aplicando DDD e padrões de microsserviços. Implementei a segurança completa da API RESTful com Spring Security e JWT. Como podem ver, ao fazer login no Frontend em React, um token é gerado pelo Backend e utilizado para autorizar todas as requisições seguintes."*

## 💻 0:30 - 1:10 | Comunicação Front x Back e Usabilidade
**O que mostrar na tela:** A aba de **Gestão de Quadras** (crie uma quadra rapidinho) e depois vá para a **Agenda** e arraste um evento (Drag & Drop).
**O que falar:**
*"Aqui no painel principal, temos toda a comunicação síncrona via requisições REST ocorrendo de forma fluida. O design da interface foi feito para ser responsivo e moderno. Na Agenda, implementamos a funcionalidade de Drag and Drop: ao arrastar uma reserva, o Frontend envia um `PUT` para o Backend que recalcula os horários e valida choques de agenda diretamente no banco de dados."*

## 🐇 1:10 - 1:50 | Mensageria (RabbitMQ) na Prática!
**O que mostrar na tela:** Volte para a aba de **Gestão de Quadras**. Deixe os logs do Docker ou a tela do RabbitMQ visível. Escreva uma avaliação e clique em Enviar.
**O que falar:**
*"O grande destaque da nossa arquitetura orientada a eventos acontece nas avaliações. Quando eu envio uma nova avaliação de quadra aqui no Front, o fluxo não é bloqueante. O Backend recebe o POST e imediatamente dispara um evento para uma fila no RabbitMQ. Em seguida, um Worker assíncrono consome essa mensagem da fila e persiste a avaliação no banco de dados. Isso traz baixo acoplamento e escalabilidade pesada pro sistema."*

## 🏁 1:50 - 2:00 | Encerramento (Docker e CI/CD)
**O que mostrar na tela:** Apenas finalize a tela do navegador.
**O que falar:**
*"Todo esse ambiente está empacotado e rodando localmente via Docker Compose. O código fonte no GitHub já conta com esteiras completas de CI/CD via GitHub Actions e manifestos do Kubernetes, garantindo automação de testes e implantação em produção. Muito obrigado!"*
