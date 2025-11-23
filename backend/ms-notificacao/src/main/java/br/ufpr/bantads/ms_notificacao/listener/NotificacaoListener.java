package br.ufpr.bantads.ms_notificacao.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import br.ufpr.bantads.ms_notificacao.DTO.ClienteDTO;
import br.ufpr.bantads.ms_notificacao.DTO.ClienteRejeitadoEvent;



@Component // este bean declara que esta classe é um componente genérico gerenciado pelo Spring
public class NotificacaoListener {

    @Value("${ms-service.url.ms-cliente}") // docker
    private String msClienteUrl;

    @Autowired // injetar javaMailSender
    private JavaMailSender mailSender;

    @Autowired // injetar restTemplate
    private RestTemplate restTemplate;

    // Ler o e-mail remetente do application.properties
    @Value("${spring.mail.from}") 
    private String remetente;
    
    
    @RabbitListener(queues = "${rabbitmq.cliente.rejeitado.queue.notificacao}")
    public void processarClienteRejeitado (ClienteRejeitadoEvent event){
        System.out.println("Recebido evento de rejeição para Cliente ID: " + event.getClienteId());
        System.out.println("Motivo: " + event.getMotivoRejeicao());

        String emailCliente = buscarEmailCliente(event.getClienteId());
        
        if(emailCliente == null || emailCliente.isEmpty()){
            System.out.println("E-mail do cliente não encontrado para o ID: " + event.getClienteId());
            // implementar lógica de fallback ou notificação alternativa aqui
            return;
        }

        String assunto = "Notificação de Rejeição de Cadastro";
        String corpo = String.format("Olá,\n\nRecebemos sua solicitação de cadastro no BANTADS.\n\n" +
            "Infelizmente, neste momento, sua solicitação foi rejeitada pelo seguinte motivo:\n" +
            "\"%s\"\n\n" +
            "Agradecemos seu interesse.\n\n" +
            "Atenciosamente,\nEquipe BANTADS",
            event.getMotivoRejeicao());
        try {   
            enviarEmail(emailCliente, assunto, corpo);
        } catch (Exception e) {
            System.out.println("Erro ao enviar e-mail para o cliente ID: " + event.getClienteId());
            e.printStackTrace();
        }
    }       


    private void enviarEmail(String emailCliente, String assunto, String corpo) {
       String para = emailCliente;
        System.out.println("Tentando enviar e-mail para: " + para);
        try {
            // instancia um SimpleMailMessage
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente); // Remetente
            message.setTo(para);         // Destinatário
            message.setSubject(assunto); // Assunto
            message.setText(corpo);      // Corpo do e-mail

            // Usar o JavaMailSender injetado para enviar
            mailSender.send(message);

            System.out.println("E-mail enviado com sucesso para: " + para);

        } catch (MailException e) { //Capturar possíveis erros durante o envio
            System.err.println("Erro ao enviar e-mail para " + para + ": " + e.getMessage());
            // Adicionar lógica de tratamento de erro conforme necessário
            // - Logar o erro de forma mais detalhada
            // - Tentar reenviar a mensagem
            // - Mover a mensagem original do RabbitMQ para uma fila de erro/DLQ
            e.printStackTrace(); // Imprime o stack trace do erro no console
        }    
    }

    private String buscarEmailCliente(Long clienteId) {
        System.out.println("BUSCA: Buscando e-mail para cliente ID: " + clienteId);
        var url = msClienteUrl + "/api/clientes/" + clienteId; // docker
        try{
        ClienteDTO cliente = restTemplate.getForObject(url, ClienteDTO.class);
        if (cliente != null && cliente.getEmail() != null) {
            return cliente.getEmail(); // Sucesso! 
        } else {
            System.err.println("Resposta do ms-cliente veio nula ou sem e-mail para ID: " + clienteId);
            return null; // Falha ao obter e-mail
        }} catch (Exception e){
            System.out.println("Erro ao buscar e-mail do cliente ID: " + clienteId + " - " + e.getMessage());
            return null;
        }
        
    }
}
