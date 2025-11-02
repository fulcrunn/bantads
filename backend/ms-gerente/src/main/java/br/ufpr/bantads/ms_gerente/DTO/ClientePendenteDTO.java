package br.ufpr.bantads.ms_gerente.DTO;
import java.math.BigDecimal;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ClientePendenteDTO {
    private Long id; // Ou String, dependendo do tipo no ms-cliente
    private String nome;
    private String cpf;
    private BigDecimal salario;
    private String motivoRejeicao;

    @Enumerated(EnumType.STRING)
    private StatusCliente status;

    public enum StatusCliente {
        PENDENTE,
        APROVADO,
        REJEITADO
    }

}
