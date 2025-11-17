package br.ufpr.bantads.ms_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuthApplication.class, args);
	}

	@Bean // Diz ao Spring: "Este método cria um objeto que eu vou gerir"
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria uma instância simples do RestTemplate
    }
}
