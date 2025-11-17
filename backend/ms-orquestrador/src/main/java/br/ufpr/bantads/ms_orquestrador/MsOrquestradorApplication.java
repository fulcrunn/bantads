package br.ufpr.bantads.ms_orquestrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsOrquestradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrquestradorApplication.class, args);
	}

	@Bean // Diz ao Spring: "Este método cria um objeto que eu vou gerir"
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria uma instância simples do RestTemplate
    }

}
