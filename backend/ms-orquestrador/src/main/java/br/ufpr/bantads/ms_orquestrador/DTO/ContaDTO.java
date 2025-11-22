package br.ufpr.bantads.ms_orquestrador.DTO;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera Getters, Setters
@AllArgsConstructor // Gera construtor com todos os argumentos
//@NoArgsConstructor  // Gera construtor vazio (essencial para o Jackson/JSON funcionar)
public class ContaDTO {

    public ContaDTO(){}

    //public ContaDTO(Long id,Long idCliente,String numConta,BigDecimal saldo,BigDecimal limite,Long idGerente,Date abertura){
    //    this.idCliente = idCliente;
    //}

    private Long id;
    private Long idCliente;
    private Long idGerente;
    private String numConta;
    private BigDecimal saldo;
    private BigDecimal limite;   
    private Date abertura;
    @Enumerated(EnumType.STRING)
    private StatusConta status;

    public enum StatusConta{
        ATIVA,
        INATIVA
    }
    
}