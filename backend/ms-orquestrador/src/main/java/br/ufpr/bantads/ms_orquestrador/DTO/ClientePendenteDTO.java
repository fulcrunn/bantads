package br.ufpr.bantads.ms_orquestrador.DTO;

import java.math.BigDecimal;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ClientePendenteDTO {
    private Long idCliente; // Ou String, dependendo do tipo no ms-cliente´p
    private Long idGerente;
    private String nome;
    private String cpf;
    private String email;
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
