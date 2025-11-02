package br.ufpr.bantads.ms_notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsNotificacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotificacaoApplication.class, args);
	}

	@Bean // Diz ao Spring: "Este método cria um objeto (bean) que você deve gerenciar"
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria uma instância simples do RestTemplate
    }

}
