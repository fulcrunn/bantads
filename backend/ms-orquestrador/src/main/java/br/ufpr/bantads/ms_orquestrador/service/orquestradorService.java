package br.ufpr.bantads.ms_orquestrador.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

import br.ufpr.bantads.ms_orquestrador.DTO.AuthDTO;
import br.ufpr.bantads.ms_orquestrador.DTO.Cliente;
import br.ufpr.bantads.ms_orquestrador.DTO.ClienteAprovadoEvent;
import br.ufpr.bantads.ms_orquestrador.DTO.ContaDTO;

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

    // Notificação para ms-cliente, ms-notificação, ms-conta, ms-auth
    @Value("EXCHANGE_APROVAR_CLIENTE") 
    private String sagaAprovarExchange;
    @Value("mscliente.registrar.cliente.comando") 
    private String aprovarMsClienteRoutingKey;
    @Value("msconta.registrar.cliente.comando")
    private String aprovarMsContaRoutingKey;
     @Value("msauth.registrar.cliente.comando")
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
        iniciarAutocadastro(cliente);
    }

    @RabbitListener(queues = "${rabbitmq.aprovar.queue}")
    public void aprovarCliente(ClienteAprovadoEvent evento){
        ContaDTO contaCriada;
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
        
        try{
            String urlConta = "http://localhost:8084/contas/aprovarConta";             
            // Enviamos os dados do evento (idCliente, salario, etc) e esperamos um ContaDTO de volta
            contaCriada = restTemplate.postForObject(urlConta, evento, ContaDTO.class); 

        } catch (Exception e) {
            System.err.println("SAGA Aprovar-ms-conta FALHOU: " + e.getMessage());
            return; // Para a SAGA
        }               
        System.out.println("Orquestrador enviou para fila de registro de ms-conta: " + evento.getIdCliente());
        System.out.println("Conta criada. Número: " + contaCriada.getNumConta());

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

    public void iniciarAutocadastro(Cliente cliente) {        
        // Enviar mensagem para a fila de autocadastro
        rabbitTemplate.convertAndSend(clientePendenteExchange, clientePendenteRoutingKey, cliente);                
        System.out.println("Orquestrador enviou para fila o cliente: " + cliente.getCpf());
    }
}
