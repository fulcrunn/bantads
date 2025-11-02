package br.ufpr.bantads.ms_gerente.service;

import java.util.Arrays;
import java.util.List;
import br.ufpr.bantads.ms_gerente.config.RabbitMQConfig;
import org.springframework.beans.factory.annotation.Value;
import br.ufpr.bantads.ms_gerente.DTO.ClienteRejeitadoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.ufpr.bantads.ms_gerente.DTO.ClientePendenteDTO;


@Service
public class GerenteService {

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    public GerenteService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void rejeitarCliente(Long clienteId, String motivo) {
        
    System.out.println("Solicitando rejeição do cliente ID: " + clienteId + " Motivo: " + motivo);

    ClienteRejeitadoEvent event = new ClienteRejeitadoEvent(clienteId, motivo);
    // Envia o objeto 'event' para a exchange sem uma routing key específica
    rabbitTemplate.convertAndSend(exchangeName,"", event);
    System.out.println("Mensagem de rejeição enviada para a exchange: " + exchangeName);
} // rejeitarCliente

    public List<ClientePendenteDTO> buscarClientesPendentes(){
        String url = "http://localhost:8080/api/clientes/pendentes";
        ClientePendenteDTO[] clientesArray = restTemplate.getForObject(url, ClientePendenteDTO[].class);
        
        if (clientesArray != null) {
        return Arrays.asList(clientesArray);
        } else {
            return List.of(); // Retorna uma lista vazia se a resposta for nula
        }
    } // end buscarClientesPendentes

    public void aprovarCliente(Long clienteId){ // verificar este método
        String url = "http://localhost:8080/api/clientes/" + clienteId + "/aprovar";
        restTemplate.patchForObject(url, null, Void.class);
    }
    
}
