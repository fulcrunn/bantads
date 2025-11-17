package br.ufpr.bantads.ms_auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.cliente.registrar.exchange}")
    private String authAprovadoExchange;

    @Value("${rabbitmq.msauth.registrar.routingkey}")
    private String authAprovadoRoutingKey;

    @Value("${rabbitmq.cliente.registrar.queue}")
    private String authAprovadoQueue;

    @Bean
    public Queue authAprovadoQueue(){
        return new Queue(authAprovadoQueue, true);
    }

    @Bean
    public DirectExchange authAprovadoExchange(){
        return new DirectExchange(authAprovadoExchange, true, false);
    }

    @Bean
    public Binding bindingAuth(Queue authAprovadoQueue, DirectExchange authAprovadoExchange){
        return BindingBuilder.bind(authAprovadoQueue).to(authAprovadoExchange).with(authAprovadoRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
