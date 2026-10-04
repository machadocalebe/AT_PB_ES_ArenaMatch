# Roteiro de Gravação - Vídeo Final ArenaMatch (Máx 2 minutos)

> **Dica:** O vídeo é muito curto (120 segundos). Seja direto, não perca tempo explicando código linha por linha. O foco é mostrar o **resultado da arquitetura funcionando**. Deixe tudo aberto antes de começar a gravar (Terminal, Docker Desktop, GitHub e Navegador).

---

## 🎬 0:00 - 0:15 | Introdução e Escopo
**O que mostrar na tela:** O diagrama da arquitetura (pode ser o que está no `DOCUMENTACAO_TP5.md`) ou a IDE mostrando a estrutura do projeto.
**O que falar:**
*"Olá! Este é o projeto ArenaMatch, desenvolvido ao longo do bloco. O sistema começou como um monólito simples e, através de um processo ágil, evoluiu para uma arquitetura orientada a eventos e microsserviços. O objetivo foi aplicar responsabilidade única, baixo acoplamento e DDD, utilizando Spring Boot, RabbitMQ para mensageria assíncrona e React no frontend."*

## 💻 0:15 - 0:45 | Demonstração Local (O Sistema em Ação)
**O que mostrar na tela:** Navegador dividido. De um lado a interface do React (Agenda e Quadras), do outro o painel web do RabbitMQ (http://localhost:15672) ou os logs do Docker no terminal. Faça uma reserva rapidamente.
**O que falar:**
*"Vou demonstrar o ambiente em execução local, que foi totalmente conteinerizado via Docker Compose. Aqui no Frontend, quando crio uma reserva de quadra, a API RESTful do backend recebe a requisição e publica um evento no RabbitMQ. Podemos ver a mensagem trafegando pela fila de forma assíncrona, desacoplando o recebimento da requisição do processamento posterior, como envio de notificações."*

## 🐳 0:45 - 1:15 | Monitoramento e Kubernetes (Pronto para Produção)
**O que mostrar na tela:** 
1. Endpoint do Actuator: `http://localhost:8080/actuator/health` (mostrando RabbitMQ e DB `UP`).
2. Rapidamente mostrar os arquivos dentro da pasta `k8s/` na IDE.
**O que falar:**
*"Pensando em produção, a saúde da aplicação é monitorada via Spring Actuator. Também criamos manifestos do Kubernetes, configurando Deployments, Services e um Horizontal Pod Autoscaler (HPA), garantindo que o sistema escale horizontalmente caso o consumo de CPU aumente sob carga."*

## 🚀 1:15 - 1:55 | CI/CD (Esteira Automatizada)
**O que mostrar na tela:** 
Página do seu GitHub Actions (a aba com o checkmark verde ✅). Depois clique na aba do Docker Hub mostrando as imagens hospedadas (`arenamatch-backend` e `arenamatch-frontend`).
**O que falar:**
*"Por fim, toda a cultura DevOps foi integrada. A nossa pipeline de CI/CD no GitHub Actions é acionada a cada commit na branch main. Ela roda automaticamente todos os testes unitários com JUnit e Mockito. Passando nos testes, a pipeline realiza o build multi-stage das imagens Docker e faz o push automatizado e seguro usando Secrets diretamente para o meu Docker Hub, entregando a versão mais recente pronta para deploy."*

## 🏁 1:55 - 2:00 | Encerramento
**O que mostrar na tela:** A tela inicial do seu projeto ou o GitHub.
**O que falar:**
*"Com isso, o projeto ArenaMatch atende todos os requisitos arquiteturais e de automação propostos para o bloco. Muito obrigado!"*
