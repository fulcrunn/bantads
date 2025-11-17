package br.ufpr.bantads.ms_gerente.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClienteAprovadoEvent {
    // Itens idCliente, idGerente, salario, email

    private Long idCliente; // Ou String, dependendo do tipo no ms-cliente
    private Long idGerente;
    private BigDecimal salario;
    private String email;
    private String status;

    public ClienteAprovadoEvent(){}

    public ClienteAprovadoEvent(Long idCliente,Long idGerente,BigDecimal salario,String email, String status ){
        this.idCliente = idCliente;
        this.idGerente = idGerente;
        this.salario = salario;
        this.email = email;
        this.status = status;
    }
}
