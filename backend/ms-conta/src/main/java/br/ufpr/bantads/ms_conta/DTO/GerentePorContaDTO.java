package br.ufpr.bantads.ms_conta.DTO;

import lombok.Data;

@Data
public class GerentePorContaDTO {
    Long idGerente;
    Long quantidadeContas;

    public GerentePorContaDTO() {}
    public GerentePorContaDTO(Long idGerente, Long quantidadeContas) {
        this.idGerente = idGerente;
        this.quantidadeContas = quantidadeContas;
    }
}
