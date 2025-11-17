package br.ufpr.bantads.ms_conta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter; // Importa o conversor de mensagens JSON do Spring AMQP
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;

@Configuration // Indica ao Spring que esta classe contém configurações de Beans
public class RabbitMQConfigConta {

    @Value("${rabbitmq.aprovacao.exchange}")
    private String aprovacaoExchange;
    
    @Value("${rabbitmq.aprovacao.queue.conta}")
    private String aprovacaoContaQueue;
    
    @Value("${rabbitmq.aprovacao.routingkey.conta}")
    private String aprovacaoContaRoutingKey; 

    // Bean da Exchange
    // Local central onde o "Maestro" (o Orquestrador da SAGA) vai 
    // entregar todos os pacotes relacionados à aprovação.
    @Bean
    public DirectExchange aprovacaoExchange() {
        return new DirectExchange(aprovacaoExchange, true, false); // true=durable, false=autoDelete
    }

    // Bean da Fila (exclusiva do ms-conta)
    @Bean
    public Queue aprovacaoContaQueue() {
        return new Queue(aprovacaoContaQueue, true);
    }

    // Bean do Binding (a "cola")
    @Bean
    public Binding bindingConta(Queue aprovacaoContaQueue, DirectExchange aprovacaoExchange) {
        return BindingBuilder.bind(aprovacaoContaQueue)
                             .to(aprovacaoExchange)
                             .with(aprovacaoContaRoutingKey); // Ligação pela routing key
    }

    @Bean // Define um Bean gerenciado pelo Spring
    public MessageConverter jsonMessageConverter() {
        // Retorna a instância do conversor JSON
        return new Jackson2JsonMessageConverter();
    }

   
}
