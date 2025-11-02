package br.ufpr.bantads.ms_auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
    private TipoCliente tipo;    

    public enum TipoCliente {
        USUARIO,
        GERENTE,
        ADMINISTRADOR
    }
    
}
