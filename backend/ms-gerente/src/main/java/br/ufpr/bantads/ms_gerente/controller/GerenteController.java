package br.ufpr.bantads.ms_gerente.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/clientes/{id}/rejeitar") // Usando PATCH e PathVariable para ID
        public ResponseEntity<Void> rejeitarCliente(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String motivo = payload.get("motivo");
        if (motivo == null || motivo.trim().isEmpty()) {
            // Retorna erro se o motivo não for fornecido
            return ResponseEntity.badRequest().build(); 
        }
        gerenteService.rejeitarCliente(id, motivo);
        // Retorna 200 OK (ou 202 Accepted, já que é assíncrono)
        return ResponseEntity.ok().build(); 
    }

    @PatchMapping("/clientes/{id}/aprovar") // Usando PATCH e PathVariable para ID
    public ResponseEntity<Void> aprovarCliente(@PathVariable Long id,@RequestBody ClientePendenteDTO cliente) {
        gerenteService.aprovarCliente(id, cliente);
        // Retorna 200 OK (ou 202 Accepted, já que é assíncrono)
        return ResponseEntity.ok().build(); 
    }

     @GetMapping("/gerente-menos-contas")
    public Optional<Long> getGerenteMenosContas() {
       
        return gerenteService.findGerenteComMenosContas();
    }
}
