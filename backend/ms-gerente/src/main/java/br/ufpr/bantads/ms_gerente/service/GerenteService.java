package br.ufpr.bantads.ms_gerente.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.ufpr.bantads.ms_gerente.DTO.ClientePendenteDTO;

@Service
public class GerenteService {

    private final RestTemplate restTemplate;

    public GerenteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ClientePendenteDTO> buscarClientesPendentes(){
        String url = "http://localhost:8080/api/clientes/pendentes";
        ClientePendenteDTO[] clientesArray = restTemplate.getForObject(url, ClientePendenteDTO[].class);
        
        if (clientesArray != null) {
        return Arrays.asList(clientesArray);
        } else {
            return List.of(); // Retorna uma lista vazia se a resposta for nula
        }
    }
    
}
