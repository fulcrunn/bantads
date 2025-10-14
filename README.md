<h1>Diagrama de casos de uso</h1>
<div>
<p>Diagrama de casos de uso de acordo com os requisitos especificados.</p>
<p>Foi utilizado o 
  <a href="https://www.plantuml.com/">PlantUml</a> para desenhar o diagrama.
</p>
<ul>
  <li>Administrador</li>
  <li>Cliente</li>
  <li>Gerente</li>
</ul>
</div>
<div>
<p styles="bold">
  <strong>Versão simplificada</strong>
</p>  
  <img width="437" height="1548" alt="image" src="https://github.com/user-attachments/assets/bbd2c046-0e35-4a60-9133-4a31e0585e27" />
<p>
  <strong>Vesão detalhada</strong>
</p>
  
  <img width="1095" height="1612" alt="image" src="https://github.com/user-attachments/assets/6a662185-a4ce-4b82-b10c-8a9ef1c9b872" />
</div>

<div class="diagramaDeClasses">
  <h1>Diagrama de Classes</h1>
  <p>
    <img width="762" height="1115" alt="image" src="https://github.com/user-attachments/assets/40e9b01b-1981-4291-b292-ed755448105e" />
  </p>  
  <p>** Vericar as particulariedades das consultas no GerenteService</p> 
</div>

<div class="diagramaDeSequencia">
  <h1>Diagrama de Sequência -> Cliente Autocadastro</h1>
  <p>Sequência:</p>
  <ul>
    <li>Cliente faz o autocadastro via página inicial (sem login).</li>
    <li>O sistema envia a solicitação para o MS Cliente.</li>
    <li>O MS Cliente cria um registro do cliente em estado "pendente".</li>
    <li>O MS Cliente aciona o MS Gerente para escolher automaticamente o gerente com menos clientes.</li>
    <li>O MS Autenticação cria um registro de login (sem liberar senha ainda).</li>
    <li>O MS Conta prepara a conta (mas só é ativada após aprovação do gerente).</li>
    <li>O sistema retorna uma mensagem “solicitação enviada”.</li>
    <li>Se houver falha, envia e-mail de erro.</li>
  </ul>
  <p>
    <img width="1446" height="620" alt="image" src="https://github.com/user-attachments/assets/25847d2b-1627-4ce7-857a-740d96c88969" />
  </p>
</div>

<div class="diagramaDeSequencia">
  <h1>Diagrama de Sequência -> Gerente aprova Cliente</h1>
  <p>Sequência:</p>
  <ul>
    <li>O Gerente acessa sua tela inicial (supondo que já está logado).</li>
    <li>Seleciona um cliente pendente e clica em Aprovar. (**visto que era o gerente com menos clientes)</li>
    O sistema gera:
    <li>Número da conta (4 dígitos aleatórios).</li>
    <li>Senha aleatória do cliente.</li>
    <li>O MS Conta cria a conta com limite baseado no salário (metade do salário).</li>
    <li>O MS Autenticação ativa o usuário e atualiza a senha.</li>
    <li>Um email é enviado ao cliente com a senha.</li>
    <li>O sistema confirma a aprovação ao Gerente.</li>
    <li>Se necessário envia notificação de erro</li>
  </ul>
  <p>
    <img width="1565" height="632" alt="image" src="https://github.com/user-attachments/assets/33d8a0ec-8eb0-4f4d-b765-9b87379796ff" />
  </p>
</div>

<div class="diagramaDeSequencia">
  <h1>Diagrama de Sequência -> Rejeitar Cliente</h1>
  <p>Sequência:</p>
  <ul>
    <li>O gerente seleciona um cliente pendente e clica em Rejeitar.</li>
    <li>O sistema solicita um motivo de rejeição.</li>
    <li>O MS Gerente registra a rejeição (com data/hora).</li>
    <li>O MS Cliente é atualizado para status "rejeitado".</li>
    <li>Um email é enviado ao cliente, informando o motivo da rejeição.</li>
    <li>O sistema confirma ao Gerente que a operação foi concluída.</li>
  </ul>
  <p>
    <img width="1475" height="472" alt="image" src="https://github.com/user-attachments/assets/4759773f-1d54-411e-ba68-6c8cffe6a6d5" />
  </p>
</div>

<div class="diagramaDeSequencia">
  <h1>Diagrama de Sequência -> Transferência</h1>
  <p>Sequência:</p>
  <ul>
    <li>Cliente solicita transferência.</li>
    <li>O sistema precisa validar saldo + limite.</li>  
    Se aprovado, registra duas movimentações:
    <li>Débito na conta de origem.</li>
    <li>Crédito na conta de destino.</li>
    <li>Usa padrão SAGA Orquestrada para garantir consistência entre contas.</li>
  </ul>
  <p>
    <img width="1379" height="848" alt="image" src="https://github.com/user-attachments/assets/8daec9bd-1e26-4089-bcd8-8fb5ed59f1ef" />
  </p>  
</div>

<div>
  <h1>Diagrama de Sequência -> Consulta de Extrato</h1>p
  <p>Sequência:</p>
  <ul>
    <li>O Cliente solicita o extrato informando data início e fim.</li>
    <li>O MS Conta (no modelo CQRS) consulta a base de leitura.</li>
    <li>O sistema retorna as movimentações no período, coloridas:</li>
    <li>Vermelho → saída (saque, transferência enviada).</li>
    <li>Azul → entrada (depósito, transferência recebida).</li>
    <li>Também retorna o saldo consolidado de cada dia (mesmo sem movimentações).</li>
    <p>
      <img width="1222" height="498" alt="image" src="https://github.com/user-attachments/assets/824df1e1-bde9-4a66-803d-f7c6193e308e" />
    </p>
  </ul>
</div>
<div>
  <h1>Diagrama de Sequência -> Inserção de Gerente</h1>
  <p>Sequência:</p>
  <ul>
    <li>Administrador insere novo Gerente.</li>
    <li>Sistema escolhe o gerente com mais contas para transferir uma delas ao novo gerente (ou trata caso especial se for o primeiro).</li>
    <li>Novo Gerente recebe a conta atribuída.</li>
    <li>Registro criado no MS Gerente e também no MS Autenticação (com login/senha).</li>
  </ul>
  <p>
    <img width="1312" height="614" alt="image" src="https://github.com/user-attachments/assets/b6fd959b-9836-45c4-8026-c221fcc8e51b" />
  </p>
</div>
<div>
  <h1>Diagrama de Sequência -> Remoção de Gerente</h1>
  <p>Sequência:</p>
  <ul>
    <li>Administrador solicita remoção.</li>
    <li>Sistema consulta gerente com menos contas para redistribuir as contas.</li>
    <li>Contas são transferidas para esse gerente.</li>
    <li>Gerente é removido do MS Gerente e MS Autenticação.</li>
    <li>Não é permitido remover o último gerente do banco.</li>
  </ul>
  <p>
    <img width="1574" height="748" alt="image" src="https://github.com/user-attachments/assets/e0d27e73-b06c-4976-bdc3-be460af7375a" />
  </p>
</div>

<div>
  <h1>Diagrama de Sequência -> Login/Logout </h1>
  <p>Sequência:</p>
  <ul>
    <li>O Cliente/Gerente/Administrador faz login com e-mail/senha.</li>
    <li>O MS Autenticação (MongoDB) valida usuário e senha.</li>
    <li>Se válido → retorna token JWT, tipo do token, tipo do usuário e os dados do usuário.</li>
    <li>Se inválido → retorna erro.</li>
    <li>O Logout é simples: basta o cliente descartar o token (não há sessão no servidor).</li>
  </ul>
  <p>
    Login:
    <img width="1088" height="647" alt="image" src="https://github.com/user-attachments/assets/48702650-18e5-4e3b-bff6-5493c7f5141b" />
  </p>
  <p>
    Logout:
    <img width="475" height="281" alt="image" src="https://github.com/user-attachments/assets/58dc9ca5-07f1-4377-b9e3-c9be027af83a" />
  </p>
</div>

<div>
  <h1>Diagrama de Componentes</h1>
  <p>Sequência:</p>
  <ul>
    <li>Frontend (Angular/React/Vue) acessa somente o API Gateway.</li>
    <li>API Gateway (Node.js) faz roteamento para os microsserviços.</li>
    Microsserviços:
    <li>MS Cliente → PostgreSQL</li>
    <li>MS Conta → PostgreSQL (CQRS: escrita + leitura)</li>
    <li>MS Gerente → PostgreSQL</li>
    <li>MS Autenticação → MongoDB</li>
    <li>Mensageria: RabbitMQ (usado em SAGA e CQRS).</li>
  </ul>
  <p>
    <img width="1282" height="559" alt="image" src="https://github.com/user-attachments/assets/31968479-aa51-4850-a311-80408d54e4a9" />
  </p>
</div>

<div>
  <h1>DER</h1>
  <p>
    <img width="473" height="831" alt="image" src="https://github.com/user-attachments/assets/368592d1-189b-448d-adf5-5e9505e77229" />
  </p>
</div>

<div>
  <h1>Organograma das pastas
    <p>
      <img width="808" height="513" alt="image" src="https://github.com/user-attachments/assets/f7ae6667-5fd7-4b1a-8286-d554d544cc11" />
    </p>
</div>
