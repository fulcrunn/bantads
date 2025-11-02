package br.ufpr.bantads.ms_notificacao.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Diz ao Jackson para ignorar as propriedades não necessárias no JSON
public class ClienteDTO {
    private String email;

}
