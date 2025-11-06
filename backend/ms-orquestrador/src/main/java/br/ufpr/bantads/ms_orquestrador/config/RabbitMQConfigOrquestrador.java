package br.ufpr.bantads.ms_orquestrador.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;


@Configuration // Indica ao Spring que esta classe contém configurações de Beans
public class RabbitMQConfigOrquestrador {

    @Value("${rabbitmq.autocadastro.exchange}")
    private String autocadastroExchange;    
    @Value("${rabbitmq.autocadastro.routingkey}")
    private String autocadastroRoutingKey;
    @Value("${rabbitmq.autocadastro.queue}")
    private String autocadastroQueue;

    @Bean // Define um Bean gerenciado pelo Spring
    public MessageConverter jsonMessageConverter() {
        // Retorna a instância do conversor JSON
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean // Declaração da Fila para autocadastro
    public Queue autocadastroQueue() {
        // O nome vem da sua nova propriedade
        return new Queue(autocadastroQueue, true); // true = durável
    }

    @Bean //o directExchange envia as mensagens para filas específicas com base na routing key
    public DirectExchange autocadastroExchange() {
    return new DirectExchange(autocadastroExchange, true, false);
    }

    @Bean // Binding entre a fila e a exchange
        public Binding binding(Queue autocadastroQueue, DirectExchange autocadastroExchange){
        return BindingBuilder.bind(autocadastroQueue).to(autocadastroExchange).with(autocadastroRoutingKey);
        }
    
} //end rabbitmqconfigorquestrador
