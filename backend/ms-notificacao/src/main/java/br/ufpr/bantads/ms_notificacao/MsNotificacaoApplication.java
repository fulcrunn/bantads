package br.ufpr.bantads.ms_notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MsNotificacaoApplication {

    public static void main(String[] args) {
        // 🔹 Carrega variáveis do .env (apenas se existir)
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // evita erro se o .env não existir
                .load();

        // 🔹 Define SMTP_KEY como variável de sistema (caso ainda não esteja definida)
        String smtpKey = System.getenv("SMTP_KEY");
        if (smtpKey == null && dotenv.get("SMTP_KEY") != null) {
            System.setProperty("SMTP_KEY", dotenv.get("SMTP_KEY"));
            System.out.println("✅ SMTP_KEY carregado do arquivo .env");
        }

        SpringApplication.run(MsNotificacaoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
