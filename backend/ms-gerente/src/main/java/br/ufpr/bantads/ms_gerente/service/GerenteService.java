package br.ufpr.bantads.ms_gerente.service;

import java.util.Arrays;
import java.util.List;
import br.ufpr.bantads.ms_gerente.model.Gerente;
import br.ufpr.bantads.ms_gerente.repository.GerenteRepository;
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
    private final GerenteRepository gerenteRepository;

    public GerenteService(RestTemplate restTemplate, RabbitTemplate rabbitTemplate, GerenteRepository gerenteRepository) {
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.gerenteRepository = gerenteRepository;
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

    public Gerente criarNovoGerente(Gerente gerente) {
        if(gerenteRepository.findByCpf(gerente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }else if(gerenteRepository.findByEmail(gerente.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email já cadastrado");
        }
        System.out.println("Criando novo gerente: " + gerente.getNome() );
        return gerenteRepository.save(gerente);
        // Aqui você pode adicionar a lógica para salvar o gerente no banco de dados
    }
    
}
