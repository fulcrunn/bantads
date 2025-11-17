## Quick orientation for AI coding agents

This repo is a small microservices Internet-Banking project (BANTADS). The goal of this document is to give an AI agent the immediate context and concrete pointers needed to be productive.

- Big picture
  - Components: frontend (Angular) in `frontend/`, API Gateway in `apiGateway/` (Node/Express), and multiple Spring Boot microservices under `backend/`:
    - `ms-cliente` (client management, default port 8080),
    - `ms-auth` (authentication, port 8081),
    - `ms-gerente` (manager operations, gateway expects 8082),
    - `ms-conta`, `ms-notificacao`, `ms-orquestrador` (SAGA orchestrator), etc.
  - Communication: synchronous HTTP through the API Gateway for REST endpoints; asynchronous messaging via RabbitMQ for SAGA workflows and cross-service events.
  - SAGA pattern: `ms-orquestrador` acts as the orchestration center. Look at `backend/ms-orquestrador/src/main/java/.../RabbitMQConfigOrquestrador.java` and `src/main/resources/application.properties` for queue/exchange names.

- Key files to inspect for architecture and examples
  - `Diagramas.txt` — UML diagrams and sequence flows (useful to understand high-level flows like Autocadastro, Aprovar Cliente, Transfêrencia).
  - `apiGateway/gateway.js` — shows gateway routes and target ports (proxies `/api/clientes` -> `http://localhost:8080`, `/auth` -> `http://localhost:8081`, `/gerentes` -> `http://localhost:8082`).
  - RabbitMQ usage examples:
    - `backend/ms-orquestrador/src/main/resources/application.properties` (exchange/queue names and RabbitMQ host/port).
    - `backend/*/src/main/java/.../config/*RabbitMQConfig*.java` — look for `@Value("${rabbitmq.*}")` and `@RabbitListener(queues = "${...}")`.
  - Example message listener: `backend/ms-conta/src/main/java/.../contaService.java` uses `@RabbitListener` for approval commands.
  - Example frontend call: `frontend/src/app/services/cliente-service.ts` sends requests to the API Gateway.

- Developer workflows and run commands (reliable, explicit)
  - RabbitMQ (local) is required for SAGA/message flows. Default props point to `localhost:5672` (see `backend/*/src/main/resources/application.properties`). A common dev command (not in repo) is:
    - Docker (PowerShell):
      docker run -d --hostname rabbit --name rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3-management
  - Start the API Gateway (Node):
    - From `apiGateway/` run `node gateway.js` (there is no start script in package.json).
  - Start a backend microservice (Windows / Maven wrapper):
    - From `backend/ms-cliente/` (or other ms): `.\mvnw.cmd spring-boot:run` (or `./mvnw spring-boot:run` on UNIX).
    - Many services expose properties in `src/main/resources/application.properties` (ports and rabbitmq tags).
  - Start the frontend (Angular):
    - From `frontend/`: `npm install` then `npm start` (runs `ng serve`).

- Project-specific conventions and patterns
  - RabbitMQ properties follow the `rabbitmq.*` keys in `application.properties` and are used via `@Value("${rabbitmq.*}")` in Java config classes. Exchanges/queues/routing keys are stored there (see `ms-orquestrador` for canonical examples).
  - SAGA commands are modeled as RabbitMQ messages. The orchestrator moves commands to service-specific queues. Search for `QUEUE_*`, `EXCHANGE_*` and `routingkey` strings across `backend/*/src/main/resources`.
  - HTTP endpoints are fronted by the simple gateway in `apiGateway/gateway.js`; the gateway assumes services run on localhost ports (8080, 8081, 8082). When editing or adding endpoints, update the gateway if the path/port changes.
  - JSON message conversion: message converters are registered (e.g. `Jackson2JsonMessageConverter`) — prefer POJOs serialized to JSON for cross-service messages.

- Helpful quick traces an agent might do
  - To follow the Approval flow: trace `frontend` -> `apiGateway/gateway.js` -> `ms-gerente` (`backend/ms-gerente`) -> publish RabbitMQ command `rabbitmq.aprovar.*` -> `ms-conta` listener (`@RabbitListener(queues="${rabbitmq.aprovacao.queue.conta}")`). Useful files: `Diagramas.txt`, `backend/ms-gerente/service/GerenteService.java`, `backend/ms-conta/service/contaService.java`.
  - To find Rabbit topics/queues: grep for `rabbitmq.` in `backend/*/src/main/resources/application.properties` and follow `@Value` usages in `config` packages.

- Safety: what not to change lightly
  - Do not rename RabbitMQ exchange/queue property keys without coordinating changes across all microservices and `ms-orquestrador` — the system expects matching property keys.
  - The gateway uses hard-coded localhost ports; if you change a service port, update `apiGateway/gateway.js`.

If anything important is missing (CI details, exact ports for every service, or Docker Compose), tell me which parts you want added and I will iterate. Ready to patch this file if you'd like changes to wording or additional examples.
