package br.ufpr.bantads.ms_gerente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsGerenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGerenteApplication.class, args);
	}

	@Bean // Diz ao Spring: "Este método cria um objeto que eu vou gerir"
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria uma instância simples do RestTemplate
    }

}
