package br.ufpr.bantads.ms_orquestrador.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;  
import br.ufpr.bantads.ms_orquestrador.DTO.Cliente;
import br.ufpr.bantads.ms_orquestrador.DTO.ClienteAprovadoEvent;
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

    @Value("${rabbitmq.cliente.registrar.exchange}")
    private String registrarClienteExchante;
    @Value("${cliente.registrar.cliente.comando}")
    private String registrarClienteRoutingKey; 

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
            Long gerente = restTemplate.getForObject(url, Long.class);
            cliente.setIdGerente(gerente);
        } catch (Exception e) {
            System.out.println("Erro ao obter gerente com menos contas: " + e.getMessage());
            // Lógica de tratamento de erro, se necessário  
        }
        System.out.println("Orquestrador recebeu comando de autocadastro para o cliente: " + cliente.getCpf());
        iniciarAutocadastro(cliente);
    }

    @RabbitListener(queues = "${rabbitmq.aprovar.queue}")
    public void aprovarCliente(ClienteAprovadoEvent evento){
        System.out.println("Recebido comando de APROVAÇÃO para o Cliente ID: " + evento.getIdCliente() + "gerido pelo gerente " + evento.getIdGerente());
        rabbitTemplate.convertAndSend(registrarClienteExchante, registrarClienteRoutingKey, evento);                
        System.out.println("Orquestrador enviou para fila de registro de cliente: " + evento.getIdCliente());
    }

    public void iniciarAutocadastro(Cliente cliente) {        
        // Enviar mensagem para a fila de autocadastro
        rabbitTemplate.convertAndSend(clientePendenteExchange, clientePendenteRoutingKey, cliente);                
        System.out.println("Orquestrador enviou para fila o cliente: " + cliente.getCpf());
    }
}
