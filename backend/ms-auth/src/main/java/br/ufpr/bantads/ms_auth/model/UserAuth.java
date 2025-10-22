package br.ufpr.bantads.ms_auth.model;

import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Document
@Data
public class UserAuth {
    
    @Id    
    private String id;    
    private String login;
    private String senha;
    // Modificação para armazenar salt e hash da senha
    private byte[] salt;
    private byte[] hashSenha;
    
    @Enumerated(EnumType.STRING)
    //Puxo o meu método do enum TipoCliente
    private TipoCliente tipo;    

    public enum TipoCliente {
        USUARIO,
        GERENTE,
        ADMINISTRADOR
    }
    
}
