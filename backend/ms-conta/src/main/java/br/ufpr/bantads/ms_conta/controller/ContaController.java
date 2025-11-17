package br.ufpr.bantads.ms_conta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufpr.bantads.ms_conta.DTO.*;
import br.ufpr.bantads.ms_conta.model.Conta;
import br.ufpr.bantads.ms_conta.service.*;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    
}//
