package br.ufpr.bantads.ms_orquestrador.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

import br.ufpr.bantads.ms_orquestrador.DTO.AuthDTO;
import br.ufpr.bantads.ms_orquestrador.DTO.Cliente;
import br.ufpr.bantads.ms_orquestrador.DTO.ClienteAprovadoEvent;
import br.ufpr.bantads.ms_orquestrador.DTO.ContaDTO;
import br.ufpr.bantads.ms_orquestrador.DTO.ComandoCriarContaDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class orquestradorService {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

   @Value("${rabbitmq.autocadastro.exchange}")
    private String autocadastroExchange;    
    @Value("${rabbitmq.autocadastro.routingkey}")
    private String autocadastroRoutingKey;
    @Value("${rabbitmq.autocadastro.queue}")
    private String autocadastroQueue;    
    @Value("${rabbitmq.cliente.pendente.exchange}")
    private String clientePendenteExchange;
    @Value("${rabbitmq.cliente.pendente.routingkey}")   
    private String clientePendenteRoutingKey;

    // O que vai consumir (escutar)
    @Value("${rabbitmq.aprovar.queue}")
    private String clienteAprovadoQueue;
    
    @Value("${rabbitmq.conta.exchange}") 
    private String contaExchange; // Exchange para R1/Conta INATIVA
    @Value("${rabbitmq.conta.routingkey}")
    private String contaRoutingKey; // Routing Key para R1/Conta INATIVA

    // Notificação para ms-cliente, ms-notificação, ms-conta, ms-auth
    // CORREÇÃO: Usando as chaves corretas que estão no properties
    @Value("${rabbitmq.cliente.registrar.exchange}") 
    private String sagaAprovarExchange;
    @Value("${rabbitmq.mscliente.registrar.routingkey}") 
    private String aprovarMsClienteRoutingKey;
    @Value("${rabbitmq.msconta.registrar.routingkey}")
    private String aprovarMsContaRoutingKey;
    @Value("${rabbitmq.msauth.registrar.routingkey}")
    private String aprovarMsAuthRoutingKey;
   

    public orquestradorService(RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;                
    }

    // Observe que o método escuta a fila não a exchange
    // Esse é o método que está escutando QUEUE_AUTOCADASTRO_INICIAR  
    // O jsonMessageConverter, nos bastidores, converte o JSON para um tipo Cliente e entrega ao método   
    @RabbitListener(queues = "${rabbitmq.autocadastro.queue}")
    public void receberComandoAutocadastro(Cliente cliente) {
        String url = "http://localhost:8082/gerentes/gerente-menos-contas";
        try{
            Long gerenteId = restTemplate.getForObject(url, Long.class);
            cliente.setIdGerente(gerenteId);
        } catch (Exception e) {
            System.out.println("Erro ao obter gerente com menos contas: " + e.getMessage());
            // Lógica de tratamento de erro, se necessário  
        }
        System.out.println("Orquestrador recebeu comando de autocadastro para o cliente: " + cliente.getCpf());
        try{
            rabbitTemplate.convertAndSend(clientePendenteExchange, clientePendenteRoutingKey, cliente);
        }catch (Exception e){
            System.out.println("Erro na exchange clientePendenteExchante: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbitmq.aprovar.queue}")
    public void aprovarCliente(ClienteAprovadoEvent evento){
        AuthDTO authCriado;
        System.out.println("Recebido comando de APROVAÇÃO para o Cliente ID: " + evento.getIdCliente() + " gerido pelo gerente " + evento.getIdGerente());
        try{
            evento.setStatus("APROVADO");            
            rabbitTemplate.convertAndSend(sagaAprovarExchange, aprovarMsClienteRoutingKey, evento);
        } catch (Exception e) {
            System.err.println("SAGA Aprovar-ms-cliente FALHOU: " + e.getMessage());
            return; // Para a SAGA
        }               
        System.out.println("Orquestrador enviou para fila de registro de ms-cliente: " + evento.getIdCliente());
        System.out.println("Status do cliente " + evento.getIdCliente() + " é " + evento.getStatus() );
        
        // NOVO: Chamada REST para ATIVAR a conta (ms-conta)
        try{
            String urlAtivarConta = "http://localhost:8084/contas/ativar/" + evento.getIdCliente();              
            // Usa PUT para atualizar o status de INATIVA para ATIVA
            restTemplate.put(urlAtivarConta, null); 
        } catch (Exception e) {
            System.err.println("SAGA Aprovar-ms-conta FALHOU (Ativação): " + e.getMessage());
            return; // Para a SAGA
        }               
        System.out.println("Orquestrador enviou para fila de registro de ms-conta: " + evento.getIdCliente());

        try{
            String urlAuth = "http://localhost:8081/auth/criarLogin"; 
            authCriado = restTemplate.postForObject(urlAuth, evento, AuthDTO.class); 
            System.out.println(authCriado.getSenha());
        } catch (Exception e) {
            System.err.println("SAGA Aprovar-ms-auth FALHOU: " + e.getMessage());
            return; // Para a SAGA
        }               
            System.out.println("Orquestrador enviou para fila de registro de ms-auth: " + evento.getIdCliente());
    }

    @RabbitListener(queues = "${rabbitmq.cliente.criado.queue}")
        public void receberEventoClienteCriado(Cliente clienteCriado) {
        System.out.println("Orquestrador recebeu evento Cliente Criado com ID: " + clienteCriado.getId() + " - Prosseguindo para criar conta INATIVA.");
        
        // Prepara DTO para criação da Conta INATIVA
        ComandoCriarContaDTO comandoConta = new ComandoCriarContaDTO(
            clienteCriado.getId(), 
            clienteCriado.getIdGerente(), 
            clienteCriado.getSalario()
        );
        
        // CORREÇÃO: Usando as chaves R1/Conta INATIVA
        try{
            rabbitTemplate.convertAndSend(contaExchange, contaRoutingKey, comandoConta); 
            System.out.println("Comando para criar Conta INATIVA enviado para ms-conta.");
        }catch(Exception e){
            System.out.println("Houve erro no envio de conta inativa:" + e.getMessage());
        }
    }

    
}
