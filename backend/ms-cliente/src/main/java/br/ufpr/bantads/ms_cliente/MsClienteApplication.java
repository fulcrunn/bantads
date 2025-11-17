package br.ufpr.bantads.ms_cliente;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsClienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsClienteApplication.class, args);
	}

	@Bean // Diz ao Spring: "Este método cria um objeto (bean) que você deve gerenciar"
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria uma instância simples do RestTemplate
    }

	

}
