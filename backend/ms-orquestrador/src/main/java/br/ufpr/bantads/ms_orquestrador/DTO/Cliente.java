package br.ufpr.bantads.ms_orquestrador.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;


@Data // Anotação que gera automaticamente os métodos getters, setters, toString, equals e hashCode
public class Cliente {
    
    
    private Long id;    
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private BigDecimal salario;
    private String endereco;
    private String cep;
    private String complemento;
    private String numero;
    private Long idGerente;    

    @Enumerated(EnumType.STRING)
    private StatusCliente status;

    private String motivoRejeicao; // Para o requisito R11 [cite: 377]

    public enum StatusCliente {
        PENDENTE,
        APROVADO,
        REJEITADO
    }
}
