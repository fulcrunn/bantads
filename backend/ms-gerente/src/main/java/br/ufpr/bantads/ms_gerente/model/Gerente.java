package br.ufpr.bantads.ms_gerente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity // Anotação que diz ao Spring:"Esta classe é uma tabela no banco"
@Data // Anotação que gera automaticamente os métodos getters, setters, toString, equals e hashCode
public class Gerente {
    // chave primária
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Pede ao banco para gerar os IDs automaticamente
    private Long id;    
    private String cpf;
    private String nome;
    private String email;
    private String telefone;
    private String tipo;

    public Gerente() {
    }

    public Gerente(String nome, String cpf, String email, String telefone, String tipo) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
    }
    
}
