package br.ufpr.bantads.ms_conta.DTO;

import lombok.Data;

@Data
public class GerentePorContaDTO {
    Long idGerente;
    Integer quantidadeContas;

    public GerentePorContaDTO() {}
    public GerentePorContaDTO(Long idGerente, Integer quantidadeContas) {
        this.idGerente = idGerente;
        this.quantidadeContas = quantidadeContas;
    }
}
