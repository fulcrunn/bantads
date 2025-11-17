package br.ufpr.bantads.ms_conta.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ComandoCriarContaDTO {
    private Long idCliente;
    private Long idGerente;
    private BigDecimal salario;

    public ComandoCriarContaDTO() {}

    public ComandoCriarContaDTO(Long idCliente, Long idGerente, BigDecimal salario) {
        this.idCliente = idCliente;
        this.idGerente = idGerente;
        this.salario = salario;
    }
}
