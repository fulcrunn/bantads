package br.ufpr.bantads.ms_cliente.config;

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
public class RabbitMQConfigCliente {
    
    @Bean // Define um Bean gerenciado pelo Spring
    public MessageConverter jsonMessageConverter() {
        // Retorna a instância do conversor JSON
        return new Jackson2JsonMessageConverter();
    }

    @Value("${rabbitmq.cliente.rejeitado.exchange}")
    private String clienteRejeitadoExchange;
    @Value("${rabbitmq.cliente.rejeitado.queue.status}")
    private String clienteRejeitadoQueueStatus;
    @Value("${rabbitmq.autocadastro.exchange}")
    private String autocadastroExchange;
    @Value("${rabbitmq.cliente.pendente.exchange}")
    private String clientePendenteExchange;
    @Value("${rabbitmq.cliente.pendente.routingkey}")   
    private String clientePendenteRoutingKey;
    @Value("${rabbitmq.cliente.pendente.queue}")
    private String clientePendenteQueue;

    @Value("${rabbitmq.aprovar.exchange}")
    private String aprovarExchange;
    @Value("${rabbitmq.aprovar.queue.cliente}")
    private String aprovarQueueCliente;
    @Value("${rabbitmq.aprovar.routingkey.cliente}")
    private String aprovarRoutingKeyCliente;

    @Value("${rabbitmq.cliente.criado.exchange}")
    private String clienteCriadoExchange;
    @Value("${rabbitmq.cliente.criado.routingkey}")
    private String clienteCriadoRoutingKey;

    // Declaração do Exchange para cliente rejeitado
    @Bean
    public FanoutExchange clienteRejeitadoExchange() {
        // O nome deve ser exatamente o mesmo que o ms-gerente está a usar
        return new FanoutExchange(clienteRejeitadoExchange, true, true); // true = durável
    }

    // Declaração da Fila para notificações de cliente rejeitado
    @Bean
    public Queue clienteRejeitadoStatusQueue() {
        // O nome vem da sua nova propriedade
        return new Queue(clienteRejeitadoQueueStatus, true); // true = durável
    }

    // Binding entre a fila e a exchange    
    @Bean
    public Binding binding(Queue clienteRejeitadoStatusQueue, FanoutExchange clienteRejeitadoExchange) {
        // Liga (bind) a fila (queue) à exchange
        return BindingBuilder.bind(clienteRejeitadoStatusQueue).to(clienteRejeitadoExchange);
    }

    //o directExchange envia as mensagens para filas específicas com base na routing key
    @Bean
    public DirectExchange autocadastroExchange() {
    return new DirectExchange(autocadastroExchange, true, false);
    }

    @Bean
    public DirectExchange clientePendenteExchange() {
    return new DirectExchange(clientePendenteExchange, true, false);
    }

    @Bean
    public Queue clientePendenteQueue() {
        return new Queue(clientePendenteQueue, true); // true = durável
    }

    @Bean
    public Binding bindingClientePendente(Queue clientePendenteQueue, DirectExchange clientePendenteExchange) {
        return BindingBuilder.bind(clientePendenteQueue).to(clientePendenteExchange).with(clientePendenteRoutingKey);
    }

    @Bean
    public DirectExchange aprovarExchange() {
        return new DirectExchange(aprovarExchange, true, false);
    }

    @Bean
    public Queue aprovarQueueCliente() {
        return new Queue(aprovarQueueCliente, true);
    }

    @Bean
    public Binding bindingAprovarCliente(Queue aprovarQueueCliente, DirectExchange aprovarExchange) {
        return BindingBuilder.bind(aprovarQueueCliente)
                            .to(aprovarExchange)
                            .with(aprovarRoutingKeyCliente);
    }

    @Bean
    public DirectExchange clienteCriadoExchange() {
        return new DirectExchange(clienteCriadoExchange, true, false);
    }
}
