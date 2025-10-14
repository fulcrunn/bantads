package br.ufpr.bantads.ms_cliente.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.ufpr.bantads.ms_cliente.model.Cliente;
import br.ufpr.bantads.ms_cliente.repository.ClienteRepository;
import java.util.Optional;

@Service // anotação para definir que essa classe é um serviço
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
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

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }
}
