package br.ufpr.bantads.ms_gerente.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String exchangeClienteRejeitado;
    
    @Bean
    public FanoutExchange clienteRejeitadoExchange() {
        // Cria uma FanoutExchange (que envia para todos)
        return new FanoutExchange(exchangeClienteRejeitado, true, true); // true = durável
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }    
    
    
    
}
    

