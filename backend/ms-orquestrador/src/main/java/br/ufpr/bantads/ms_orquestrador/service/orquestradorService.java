package br.ufpr.bantads.ms_orquestrador.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;  
import br.ufpr.bantads.ms_orquestrador.DTO.Cliente;
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

    public orquestradorService(RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;                
    }

    // Observe que o método escuta a fila não a exchange    
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

    public void iniciarAutocadastro(Cliente cliente) {        
        // Enviar mensagem para a fila de autocadastro
        rabbitTemplate.convertAndSend(clientePendenteExchange, clientePendenteRoutingKey, cliente);                
        System.out.println("Orquestrador enviou para fila o cliente: " + cliente.getCpf());
    }
}
