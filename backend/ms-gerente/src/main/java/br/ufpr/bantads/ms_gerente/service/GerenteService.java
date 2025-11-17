package br.ufpr.bantads.ms_gerente.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import br.ufpr.bantads.ms_gerente.model.Gerente;
import br.ufpr.bantads.ms_gerente.repository.GerenteRepository;
import org.springframework.beans.factory.annotation.Value;
import br.ufpr.bantads.ms_gerente.DTO.ClienteRejeitadoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufpr.bantads.ms_gerente.DTO.ClienteAprovadoEvent;
import br.ufpr.bantads.ms_gerente.DTO.ClientePendenteDTO;
import br.ufpr.bantads.ms_gerente.DTO.GerentePorContaDTO;



@Service
public class GerenteService {

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String exchangeName;
    
    @Value("${rabbitmq.aprovar.exchange}")
    private String clienteAprovadoExchange;
    @Value("${rabbitmq.aprovar.routingkey}")
    private String clienteAprovadoRoutingKey;

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

    public void aprovarCliente(Long clienteId, ClientePendenteDTO cliente){ // verificar este método
        System.out.println("Solicitando aprovacao do cliente ID: " + clienteId);
        ClienteAprovadoEvent event = new ClienteAprovadoEvent(clienteId, cliente.getIdGerente(),cliente.getSalario(),cliente.getEmail(),"PENDENTE");  
        // Envia o objeto 'event' para a exchange sem uma routing key específica
    rabbitTemplate.convertAndSend(clienteAprovadoExchange,clienteAprovadoRoutingKey, event);
    } // end aprovarCliente

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

    public Optional<Long> findGerenteComMenosContas(){
        //IGNORANDO REQUISITO DE VERIFICAR O SALDO DO GERENTE PARA DESEMPATE, PEGAREI O COM MENOS CONTAS QUE APARECER PRIMEIROl
        String url = "http://localhost:8084/contas/contagem-por-gerente";
        // Lógica para encontrar o gerente com menos contas
        GerentePorContaDTO[] gerenteContasArray;
        try{
            gerenteContasArray = restTemplate.getForObject(url, GerentePorContaDTO[].class);
        } catch (Exception e){
            System.out.println("Erro ao chamar MS-Conta: " + e.getMessage());
            return Optional.empty();
        }
        // fim da lógica de menos contas        
        List<GerentePorContaDTO> gerenteContasList = (gerenteContasArray!=null) ? Arrays.asList(gerenteContasArray): List.of(); // condicional
        // forma do gerenteContas [{"idGerente": 1, "quantidadeContas": 5}, ...]
        System.out.println(gerenteContasArray);
        Optional<GerentePorContaDTO> gerenteComMenosContas = gerenteContasList.stream()
            .min((g1, g2) -> Long.compare(g1.getQuantidadeContas(), g2.getQuantidadeContas())); // encontra o gerente com menos contas
        Optional<Long> idGerente = gerenteComMenosContas.map(dto -> dto.getIdGerente());    

        if(idGerente.isEmpty()){
            System.out.println("Nenhum gerente com contas foi encontrado. (Há ao menos um gerente cadastrado!)");
            Optional<Gerente> primeiroGerente = gerenteRepository.findAll().stream().findFirst();
            idGerente = primeiroGerente.map(gerente -> gerente.getId());
        } 

        return idGerente;
    }
    
}
