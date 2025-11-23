package br.ufpr.bantads.ms_notificacao.config;

import org.springframework.amqp.core.FanoutExchange;
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
public class RabbitMQConfigNotificacao {
    
    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String clienteRejeitadoExchange;
    @Value("${rabbitmq.cliente.rejeitado.queue.notificacao}")
    private String clienteRejeitadoQueueNotificacao;

    @Value("${rabbitmq.cliente.aprovado.exchange}")
    private String clienteAprovadoExchange;
    @Value("${rabbitmq.cliente.aprovado.queue.notificacao}")
    private String clienteAprovadoQueueNotificacao;
    @Value("${rabbitmq.cliente.aprovado.routingkey}")
    private String clienteAprovadoRoutingKey;
   
    @Bean // Define um Bean gerenciado pelo Spring
    public MessageConverter jsonMessageConverter() {
        // Retorna a instância do conversor JSON
        return new Jackson2JsonMessageConverter();
    }
    // Declaração do Exchange para cliente rejeitado
    @Bean
    public FanoutExchange clienteRejeitadoExchange() {
        // O nome deve ser exatamente o mesmo que o ms-gerente está a usar
        return new FanoutExchange(clienteRejeitadoExchange, true, false); // true = durável
    }

    // Declaração da Fila para notificações de cliente rejeitado
    @Bean
    public Queue clienteRejeitadoNotificacaoQueue() {
        // O nome vem da sua nova propriedade
        return new Queue(clienteRejeitadoQueueNotificacao, true); // true = durável
    }

    // Binding entre a fila e a exchange    
    @Bean
    public Binding binding(Queue clienteRejeitadoNotificacaoQueue, FanoutExchange clienteRejeitadoExchange) {
        // Liga (bind) a fila (queue) à exchange
        return BindingBuilder.bind(clienteRejeitadoNotificacaoQueue).to(clienteRejeitadoExchange);
    }

    // Declaração do Exchange para cliente aprovado
    @Bean
    public DirectExchange clienteAprovadoExchange() { 
        return new DirectExchange(clienteAprovadoExchange, true, false); 
    }

    // Declaração da Fila para notificações de cliente aprovado
    @Bean
    public Queue clienteAprovadoNotificacaoQueue() {
        return new Queue(clienteAprovadoQueueNotificacao, true); // true = durável
    }

    // Binding entre a fila e a exchange de aprovação 
    @Bean
    public Binding bindingAprovacao(Queue clienteAprovadoNotificacaoQueue, DirectExchange clienteAprovadoExchange) {
        return BindingBuilder.bind(clienteAprovadoNotificacaoQueue)
                             .to(clienteAprovadoExchange)
                             .with(clienteAprovadoRoutingKey);
    }
}
