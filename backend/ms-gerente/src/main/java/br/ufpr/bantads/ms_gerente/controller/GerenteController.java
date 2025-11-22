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

    @GetMapping("/{idGerente}/clientes-pendentes")
        public List<ClientePendenteDTO> getClientesPendentesPorGerente(@PathVariable String idGerente) {
            Long gerenteId;
            try {
                // Tenta converter para Long (o ID esperado do PostgreSQL)
                gerenteId = Long.parseLong(idGerente);
            } catch (NumberFormatException e) {
                // Se a conversão falhar (porque é um ID MongoDB String), retorna lista vazia e loga.
                System.err.println("ALERTA: ID do Gerente é um ID MongoDB. A filtragem está retornando lista vazia. O mapeamento ID MongoDB -> ID Relacional falhou.");
                return List.of(); 
            }
            // Se for um Long válido, continua com a lógica
            return gerenteService.buscarClientesPendentes(gerenteId);
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

    //endpoint de ligação entre o postgres e o mongo de autenticação
    @GetMapping("/id-by-login/{login}")
    public ResponseEntity<Long> getGerenteIdByLogin(@PathVariable("login") String login) {
        return gerenteService.findIdByEmail(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} // end class
