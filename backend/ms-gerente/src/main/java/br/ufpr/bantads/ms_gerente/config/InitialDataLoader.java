package br.ufpr.bantads.ms_gerente.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import br.ufpr.bantads.ms_gerente.service.GerenteService;
import br.ufpr.bantads.ms_gerente.model.Gerente;

@Component // Não preciso instanciar o método, ele cria uma instância (um objeto) desta classe para mim quando a aplicação arrancar.
public class InitialDataLoader implements CommandLineRunner {

    private final GerenteService gerenteService;

    @Autowired
    public InitialDataLoader(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Executando DataLoader inicial!");
        criarUtilizadoresIniciais();
        System.out.println("DataLoader inicial concluído.");
    } // end run 
    
    private void criarUtilizadoresIniciais() {
        
        // Gerentes
        criarUtilizadorSeNaoExistir("Geniéve", "98574307084", "ger1@bantads.com.br", "41999998888", "GERENTE");
        criarUtilizadorSeNaoExistir("Godophredo", "64065268052", "ger2@bantads.com.br", "41988887777", "GERENTE");
        criarUtilizadorSeNaoExistir("Gyândula", "23862179060", "ger3@bantads.com.br", "41977776666", "GERENTE");
        
    }
    private void criarUtilizadorSeNaoExistir(String nome, String cpf, String email, String telefone, String tipo) {
        Gerente gerente = new Gerente(nome, cpf, email, telefone, tipo);
        try {
            // Tentamos criar o utilizador
            gerenteService.criarNovoGerente(gerente);
            System.out.println("Utilizador criado: " + nome);
        } catch (IllegalArgumentException e) {
            // Se já existe, apenas informamos (não é um erro fatal)
            System.out.println("Utilizador já existe: " + nome);
        }
    }

}
