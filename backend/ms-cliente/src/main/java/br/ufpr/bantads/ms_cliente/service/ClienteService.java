package br.ufpr.bantads.ms_cliente.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.ufpr.bantads.ms_cliente.DTO.ClienteAprovadoEvent;
import br.ufpr.bantads.ms_cliente.DTO.ClienteRejeitadoEvent;
import br.ufpr.bantads.ms_cliente.model.Cliente;
import br.ufpr.bantads.ms_cliente.model.Cliente.StatusCliente;
import br.ufpr.bantads.ms_cliente.repository.ClienteRepository;
import java.util.Optional;


@Service // anotação para definir que essa classe é um serviço
public class ClienteService {
    @Value("${rabbitmq.cliente.rejeitado.queue.status}")
    private String queueName;
    
    private final RabbitTemplate rabbitTemplate;
    private final ClienteRepository clienteRepository;
    @Value("${rabbitmq.autocadastro.exchange}")
    private String autocadastroExchange;    
    @Value("${rabbitmq.autocadastro.routingkey}")
    private String autocadastroRoutingKey;
    @Value("${rabbitmq.cliente.pendente.exchange}")
    private String clientePendenteExchange;
    @Value("${rabbitmq.cliente.pendente.routingkey}")   
    private String clientePendenteRoutingKey;
    @Value("${rabbitmq.cliente.criado.exchange}")
    private String clienteCriadoExchange;
    @Value("${rabbitmq.cliente.criado.routingkey}")
    private String clienteCriadoRoutingKey;

    public ClienteService(ClienteRepository clienteRepository,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.clienteRepository = clienteRepository;
    }
    @RabbitListener(queues = "${rabbitmq.aprovar.queue.cliente}")
        public void onAprovarCliente(ClienteAprovadoEvent evento) {
        System.out.println("MS-CLIENTE: Recebido comando para APROVAR cliente ID: " + evento.getIdCliente());
        
        @SuppressWarnings("null")
        Optional<Cliente> clienteOpt = clienteRepository.findById(evento.getIdCliente());
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setStatus(StatusCliente.APROVADO); // A MUDANÇA DE STATUS!
            clienteRepository.save(cliente);
            System.out.println("MS-CLIENTE: Cliente ID: " + evento.getIdCliente() + " atualizado para APROVADO.");
        } else {
            System.err.println("MS-CLIENTE: Cliente ID: " + evento.getIdCliente() + " não encontrado para aprovação.");
            
        }
    }
    
    @RabbitListener(queues = "${rabbitmq.cliente.rejeitado.queue.status}")
     public void rejeitarCliente(ClienteRejeitadoEvent event) {        
        System.out.println("Rejeitando cliente ID: " + event.getClienteId() + " Motivo: " + event.getMotivoRejeicao());
        @SuppressWarnings("null")
        Optional<Cliente> clienteOpt = clienteRepository.findById(event.getClienteId());
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setStatus(StatusCliente.REJEITADO);
            cliente.setMotivoRejeicao(event.getMotivoRejeicao());
            clienteRepository.save(cliente);
        } else {
            System.out.println("Cliente ID: " + event.getClienteId() + " não encontrado para rejeição.");
        }
    }

    // Método para criar um novo cliente iniciando a SAGA de autocadastro
    public void criarCliente(Cliente cliente) {
        if(clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
            
        }else if(clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        System.out.println("Enviando comando para SAGA (Autocadastro) para o cliente: " + cliente.getCpf());
        rabbitTemplate.convertAndSend(autocadastroExchange, autocadastroRoutingKey, cliente);
        // Observar que nesse ponto, nos bastidores, o jsonMessageConverter converte o cliente em um JSON
        // Depois atribui a esse "pacote cliente" uma etiqueta que é autocadastro.iniciar.comando        
    }

    @RabbitListener(queues ="${rabbitmq.cliente.pendente.queue}")
    public void cadastrarClientePendente(Cliente cliente) {
        System.out.println("Cadastrando cliente pendente ID: " + cliente.getId() + " CPF: " + cliente.getCpf());
        cliente.setStatus(StatusCliente.PENDENTE);
        // salva a instância com o ID gerado pelo banco
        Cliente clienteSalvo = clienteRepository.save(cliente);
        System.out.println("Publicando evento Cliente Criado com ID: " + clienteSalvo.getId());
        rabbitTemplate.convertAndSend(clienteCriadoExchange, clienteCriadoRoutingKey, clienteSalvo);
        System.out.println("Cliente pendente salvo com sucesso: " + clienteSalvo.getId());
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
          }
          
     public List<Cliente> getClientesPorStatus(Cliente.StatusCliente status) {
        return clienteRepository.findByStatus(status);
          }       

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }
    //ritorna o uma lista de clientes dado o gerente
    public List<Cliente> getClientesPendentesPorGerente(Long idGerente) {
    return clienteRepository.findByStatusAndIdGerente(Cliente.StatusCliente.PENDENTE, idGerente);
}
}
