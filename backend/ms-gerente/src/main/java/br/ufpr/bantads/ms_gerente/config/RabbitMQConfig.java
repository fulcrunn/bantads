package br.ufpr.bantads.ms_gerente.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.cliente.rejeitado.queue}")
    private String queueClienteRejeitado;
    @Bean
    public Queue clienteRejeitadoQueue() {
        // Cria a fila se ela não existir
        return new Queue(queueClienteRejeitado, true); // true = durável
    }
    
}
