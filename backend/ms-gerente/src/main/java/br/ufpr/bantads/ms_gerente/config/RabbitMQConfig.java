package br.ufpr.bantads.ms_gerente.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    // O produtor só declara a exchange
    @Value("${rabbitmq.aprovar.exchange}")
    private String clienteAprovadoExchange;
    
    // Declaração do Exchange para cliente aprovado
    @Bean
    public DirectExchange clienteAprovadoExchange(){
        return new DirectExchange(clienteAprovadoExchange, true, false);
    }    

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String exchangeClienteRejeitado;
    
    @Bean
    public FanoutExchange clienteRejeitadoExchange() {
        // Cria uma FanoutExchange (que envia para todos)
        return new FanoutExchange(exchangeClienteRejeitado, true, false); // true = durávelp
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }    
    
}
    
