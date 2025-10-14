package br.ufpr.bantads.ms_cliente.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufpr.bantads.ms_cliente.repository.ClienteRepository;
import br.ufpr.bantads.ms_cliente.service.ClienteService;
import br.ufpr.bantads.ms_cliente.model.*;

@RestController
@RequestMapping("/api/clientes/")
public class ClienteController {
    
    @Autowired // injecção de dependência
    private ClienteService clienteService;    

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        try{
            Cliente novoCliente = clienteService.criarCliente(cliente);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map <String,String> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
                
        return clienteService.getClienteById(id).map(cliente -> ResponseEntity.ok().body(cliente))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)  );
    }
    
}
