package br.ufpr.bantads.ms_notificacao.DTO;

import java.io.Serializable;

public class ClienteRejeitadoEvent {
    private Long clienteId;
    private String motivoRejeicao;

    public ClienteRejeitadoEvent() {}
    
    public ClienteRejeitadoEvent(Long clienteId, String motivoRejeicao) {
        this.clienteId = clienteId;
        this.motivoRejeicao = motivoRejeicao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }
    
    // Setters podem ser úteis para frameworks de serialização
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }
}
