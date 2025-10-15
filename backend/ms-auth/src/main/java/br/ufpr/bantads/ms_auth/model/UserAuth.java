package main.java.br.ufpr.bantads.ms_auth.model;

@Document
@Data
public class UserAuth {
    @Id    
    private String id;    
    private String login;
    private String senha;
    
    @Enumerated(EnumType.STRING)
    //Puxo o meu método do enum TipoCliente
    private TipoCliente tipo;    

    public enum TipoCliente {
        USUARIO,
        GERENTE,
        ADMINISTRADOR
    }
}
