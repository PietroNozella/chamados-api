# Chamados API

Backend da atividade de chamados técnicos, com Java 21, Spring Boot 3.4.0,
Spring Security e MongoDB. O funcionamento será demonstrado pelo Postman ou
Insomnia, sem interface gráfica.

## Estado atual

A estrutura inicial está na branch `chore/estrutura-inicial`. Ela mantém os
modelos, repositórios, serviços e validações de usuários reaproveitados da base,
com seus testes. Templates, arquivos estáticos, controllers de páginas,
redirecionamentos de login e o envio de e-mail foram retirados.

Os endpoints de autenticação e de chamados ainda não estão implementados.
Todas as rotas estão bloqueadas nesta etapa. Requisições GET sem autenticação
recebem HTTP 401; não há formulário de login. A proteção CSRF permanece ativa.

O código de recuperação de senha e aceite legal ainda existente nos serviços e
modelos veio da base e não possui endpoints expostos neste projeto.

## Origem

Base: [PietroNozella/login-java](https://github.com/PietroNozella/login-java),
commit [`f76d857`](https://github.com/PietroNozella/login-java/commit/f76d857ade77cc46afbedb0db88eae51fda5ebcd).
O histórico Git foi preservado. O remoto `upstream` identifica a origem e está
com envio desabilitado nesta cópia. O remoto `origin` aponta para o novo repositório
[PietroNozella/chamados-api](https://github.com/PietroNozella/chamados-api).

## Execução com Docker

Pré-requisitos: Docker Desktop em execução e Docker Compose disponível.

```powershell
Copy-Item .env.example .env
# Edite .env e preencha APP_ADMIN_PASSWORD antes de continuar.
docker compose up --build
```

- Aplicação: `http://localhost:8081`.
- MongoDB no computador: `mongodb://localhost:27019/chamados_api`.
- MongoDB dentro do Compose: `mongodb://mongo:27017/chamados_api`.
- Banco padrão: `chamados_api`.
- Volume próprio do projeto: `chamados-api_mongo-data`.

A aplicação cria a primeira conta administrativa se ela ainda não existir.
A senha vem de `APP_ADMIN_PASSWORD`, sem padrão embutido. Não há login via API
nesta etapa. O banco e o volume são separados dos projetos de origem.
`docker compose down` encerra os serviços e preserva o volume.

## Execução local

Pré-requisitos: JDK 21 e MongoDB disponível. É possível iniciar somente o banco
com `docker compose up -d mongo`, após preparar `.env` conforme acima.

```powershell
$env:MONGODB_URI='mongodb://localhost:27019/chamados_api'
$env:MONGODB_DATABASE='chamados_api'
$env:APP_ADMIN_PASSWORD='defina-uma-senha-local'
.\mvnw.cmd spring-boot:run
```

O Maven Wrapper fornece o Maven. Na execução local, `.env` não é carregado
automaticamente; use variáveis de ambiente do processo. `SERVER_PORT` altera
a porta da aplicação local, cujo padrão é 8081.

## Validação

```powershell
.\mvnw.cmd clean verify
docker compose config --quiet
```

Os testes de serviços usam repositórios simulados e as validações exercitam
Bean Validation. Dois testes com MockMvc verificam o bloqueio da API e a ausência
de formulário de login. A suíte não precisa de MongoDB e não comprova persistência
real nem chamadas pela rede. O JAR é gerado em
`target/chamados-api-1.0.0.jar`.

## Estrutura

O código fica em `src/main/java/com/example/chamados`:

- Raiz: inicialização, segurança, BCrypt e conta administrativa inicial.
- `dto`: validações de entrada reaproveitadas.
- `entity`: documentos do MongoDB.
- `repository`: acesso ao banco.
- `service`: regras de usuários e componentes de autenticação reaproveitados.

Os controllers REST serão acrescentados nas respectivas features. A configuração
fica em `src/main/resources/application.properties` e os testes em `src/test/java`.

## Branches e próximas entregas

Cada entrega usa uma branch própria. Após revisão e merge do PR, a próxima
branch parte da `main` atualizada. Não é necessário manter branches vazias.

| Branch | Entrega | Situação |
| --- | --- | --- |
| `chore/estrutura-inicial` | Base backend, ambiente e instruções | Etapa atual |
| `feat/autenticacao` | Acesso pela API e permissões de usuário e técnico | Pendente |
| `feat/abertura-chamados` | Título, descrição, solicitante, abertura e status automáticos | Pendente |
| `feat/consulta-chamados` | Listagem por usuário e consulta individual | Pendente |
| `feat/resolucao-chamados` | Solução, data e bloqueio de alterações após resolução | Pendente |
| `docs/demonstracao-api` | Coleção de requisições e roteiro do vídeo | Pendente |

Endpoints exigidos, ainda pendentes:

```text
POST /api/chamados
GET  /api/chamados/usuario/{id}
GET  /api/chamados/{id}
PUT  /api/chamados/{id}/resolver
```

A demonstração final deve incluir autenticação, persistência no MongoDB e rejeição
de alterações em chamados resolvidos, em uma gravação de tela de até cinco minutos.
