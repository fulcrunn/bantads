package br.ufpr.bantads.ms_conta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufpr.bantads.ms_conta.DTO.*;
import br.ufpr.bantads.ms_conta.model.Conta;
import br.ufpr.bantads.ms_conta.service.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private contaService contaService;

    @GetMapping("/contagem-por-gerente")
    public List<GerentePorContaDTO> gerentesPorConta() {
        return contaService.getGerentePorConta();
    }

    @PostMapping("/aprovarConta")
    public Conta postMethodName(@RequestBody ClienteAprovadoEvent cliente) {                
        return contaService.criarConta(cliente.getIdCliente(),cliente.getSalario(),cliente.getIdGerente());
    }
    
    @PutMapping("/ativar/{idCliente}") 
    public ResponseEntity<Void> ativarConta(@PathVariable Long idCliente) {
        contaService.ativarConta(idCliente);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ContaDTO> getContaByClienteId(@PathVariable Long idCliente) {
        Conta conta = contaService.getContaByClienteId(idCliente);
        if (conta != null) {
            ContaDTO dto = new ContaDTO(); 
            dto.setSaldo(conta.getSaldo());
            dto.setLimite(conta.getLimite());
            dto.setIdCliente(conta.getIdCliente());
            dto.setNumConta(conta.getNumConta());
            
            return ResponseEntity.ok(dto);
        } else {
            // Erro de Conta não encontrada
            System.out.println("ERRO - Conta não encontrada! (Vide ms-conta, ContaController.java)"); 
            return ResponseEntity.notFound().build(); 
        }
    }
    
}//
