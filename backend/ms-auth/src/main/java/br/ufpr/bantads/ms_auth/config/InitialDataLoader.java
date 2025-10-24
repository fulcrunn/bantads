package br.ufpr.bantads.ms_auth.config;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.ufpr.bantads.ms_auth.model.UserAuth;
import br.ufpr.bantads.ms_auth.service.AuthService;

@Component // Não preciso instanciar o método, ele cria uma instância (um objeto) desta classe para mim quando a aplicação arrancar.
public class InitialDataLoader implements CommandLineRunner {
    
    private final AuthService authService;
    @Autowired
    public InitialDataLoader(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executando DataLoader inicial!");    

        criarUtilizadoresIniciais();
        System.out.println("DataLoader inicial concluído.");
    } // run
    
    private void criarUtilizadoresIniciais() {
        // Administrador
        criarUtilizadorSeNaoExistir("admin@bantads.com.br", "admin", UserAuth.TipoCliente.ADMINISTRADOR);
        // Gerentes
        criarUtilizadorSeNaoExistir("gerente1@bantads.com.br", "tads", UserAuth.TipoCliente.GERENTE);
        criarUtilizadorSeNaoExistir("gerente2@bantads.com.br", "tads", UserAuth.TipoCliente.GERENTE);
        criarUtilizadorSeNaoExistir("gerente3@bantads.com.br", "tads", UserAuth.TipoCliente.GERENTE);
        
    }
    private void criarUtilizadorSeNaoExistir(String login, String senha, UserAuth.TipoCliente tipo) {
        try {
            // Tentamos criar o utilizador
            authService.createNewUser(login, senha, tipo);
            System.out.println("Utilizador criado: " + login);
        } catch (IllegalArgumentException e) {
            // Se já existe, apenas informamos (não é um erro fatal)
            System.out.println("Utilizador já existe: " + login);
        }
    }
    
}
