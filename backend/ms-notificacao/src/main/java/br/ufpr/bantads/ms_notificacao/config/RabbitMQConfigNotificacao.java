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

@Configuration // Indica ao Spring que esta classe contém configurações de Beans
public class RabbitMQConfigNotificacao {
    
    @Bean // Define um Bean gerenciado pelo Spring
    public MessageConverter jsonMessageConverter() {
        // Retorna a instância do conversor JSON
        return new Jackson2JsonMessageConverter();
    }

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String clienteRejeitadoExchange;
    @Value("${rabbitmq.cliente.rejeitado.queue.notificacao}")
    private String clienteRejeitadoQueueNotificacao;

    // Declaração do Exchange para cliente rejeitado
    @Bean
    public FanoutExchange clienteRejeitadoExchange() {
        // O nome deve ser exatamente o mesmo que o ms-gerente está a usar
        return new FanoutExchange(clienteRejeitadoExchange, true, true); // true = durável
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
}
