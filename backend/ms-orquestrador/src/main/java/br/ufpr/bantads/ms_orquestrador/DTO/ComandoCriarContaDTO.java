package br.ufpr.bantads.ms_orquestrador.DTO;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComandoCriarContaDTO {
    private Long idCliente;
    private Long idGerente;
    private BigDecimal salario;
}