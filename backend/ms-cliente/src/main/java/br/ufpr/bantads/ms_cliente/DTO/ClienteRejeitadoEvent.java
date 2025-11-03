package br.ufpr.bantads.ms_cliente.DTO;

import lombok.Data;

@Data
public class ClienteRejeitadoEvent {

    private Long clienteId;
    private String motivoRejeicao;

    public ClienteRejeitadoEvent() {}
    
    public ClienteRejeitadoEvent(Long clienteId, String motivoRejeicao) {
        this.clienteId = clienteId;
        this.motivoRejeicao = motivoRejeicao;
    }
    
}
