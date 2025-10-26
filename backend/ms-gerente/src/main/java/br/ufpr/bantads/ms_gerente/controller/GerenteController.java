package br.ufpr.bantads.ms_gerente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufpr.bantads.ms_gerente.DTO.ClientePendenteDTO;
import br.ufpr.bantads.ms_gerente.service.GerenteService;


@RestController
@RequestMapping("/gerentes")
public class GerenteController {
    
    private final GerenteService gerenteService;

    @Autowired
    public GerenteController(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
        
    }   

    @GetMapping("/clientes-pendentes")
    public List<ClientePendenteDTO> getAllClientesPendentes() {
       
        return gerenteService.buscarClientesPendentes();
    }
}
