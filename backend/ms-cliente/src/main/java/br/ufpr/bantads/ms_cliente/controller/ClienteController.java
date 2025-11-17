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
import br.ufpr.bantads.ms_cliente.service.ClienteService;
import br.ufpr.bantads.ms_cliente.model.*;

@RestController // indica que essa classe é um controlador REST, ou seja, que ela vai responder a requisições HTTP e expor endpoints da API
@RequestMapping("/api/clientes/")
public class ClienteController {
    
    @Autowired // injecção de dependência
    private ClienteService clienteService;    

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @GetMapping("/pendentes")
    public List<Cliente> getClientesPendentes() {
        return clienteService.getClientesPorStatus(Cliente.StatusCliente.PENDENTE);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        try{
            clienteService.criarCliente(cliente);
            // Cria uma estrutura para a resposta que será usada no frontend
            Map <String,String> resposta = new HashMap<>();
            // respota(key:value)
            resposta.put("mensagem", "Solicitação de autocadastro recebida e em processamento.");
            return new ResponseEntity<>(resposta, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            // Cria uma estrutura para a resposta que será usada no frontend
            Map <String,String> erro = new HashMap<>();
            erro.put("mensagem", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable("id") Long id) {
                
        return clienteService.getClienteById(id).map(cliente -> ResponseEntity.ok().body(cliente))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)  );
    }
    
}
