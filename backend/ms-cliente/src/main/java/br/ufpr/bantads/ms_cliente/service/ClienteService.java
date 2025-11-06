package br.ufpr.bantads.ms_cliente.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public ClienteService(ClienteRepository clienteRepository,RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.clienteRepository = clienteRepository;
    }
    
    @RabbitListener(queues = "${rabbitmq.cliente.rejeitado.queue.status}")
     public void rejeitarCliente(ClienteRejeitadoEvent event) {        
        System.out.println("Rejeitando cliente ID: " + event.getClienteId() + " Motivo: " + event.getMotivoRejeicao());
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

    public Cliente criarCliente(Cliente cliente) {
        if(clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
            
        }else if(clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        return clienteRepository.save(cliente);
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
}
