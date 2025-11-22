package br.ufpr.bantads.ms_conta.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity // Anotação que diz ao Spring:"Esta classe é uma tabela no banco"
@Data // Anotação que gera automaticamente os métodos getters, setters, toString, equals e hashCode
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Pede ao banco para gerar os IDs automaticamente
    private Long id;
    
    private Long idCliente;
    private String numConta;
    private BigDecimal saldo;
    private BigDecimal limite;
    private Long idGerente;
    private Date abertura;
    @Enumerated(EnumType.STRING)
    private StatusConta status;

    public enum StatusConta{
        ATIVA,
        INATIVA
    }


}
