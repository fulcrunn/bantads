package br.ufpr.bantads.ms_auth.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import br.ufpr.bantads.ms_auth.DTO.AuthDTO;
import br.ufpr.bantads.ms_auth.DTO.ClienteAprovadoEvent;
import br.ufpr.bantads.ms_auth.model.UserAuth;
import br.ufpr.bantads.ms_auth.service.AuthService;
import br.ufpr.bantads.ms_auth.service.JwtService;
import br.ufpr.bantads.ms_auth.service.PasswordService;


@RestController
@RequestMapping("/auth")

public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private PasswordService passwordService;
    
    @Value("${ms-service.url.ms-gerente}") // Docker
    private String msGerenteUrl;

    /*@Autowired // remover após testes
    public AuthController(AuthService authService, JwtService jwtService,PasswordService passwordService) { 
        this.authService = authService;
        this.jwtService = jwtService;
        this.passwordService = passwordService; 
    }*/
    @Autowired 
    public AuthController(AuthService authService, JwtService jwtService, RestTemplate restTemplate,PasswordService passwordService) { 
        this.authService = authService;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate; 
        this.passwordService = passwordService;
    }
    
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> loginAuth(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserAuth usuarioAutenticado = authService.authenticate(loginRequestDTO.getLogin(), loginRequestDTO.getSenha());
        if (usuarioAutenticado != null) {
            String token = jwtService.generateToken(usuarioAutenticado.getLogin(), usuarioAutenticado.getTipo().name());
            
            // Variável para armazenar o ID correto (relacional ou MongoDB)
            String finalId = usuarioAutenticado.getId(); 
            
            // SE o usuário for GERENTE, buscamos o ID Relacional (Long)
            if (usuarioAutenticado.getTipo() == UserAuth.TipoCliente.GERENTE) {
                String login = usuarioAutenticado.getLogin();
                String url = msGerenteUrl + "/gerentes/id-by-login/" + login; // Docker
                try {
                    // Espera um Long e converte para String para o JSON
                    Long gerenteRelationalId = restTemplate.getForObject(url, Long.class);
                    if (gerenteRelationalId != null) {
                        finalId = gerenteRelationalId.toString(); 
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao buscar ID relacional do gerente: " + e.getMessage());
                    // Mantém o ID MongoDB como fallback, mas o filtro no ms-gerente/ms-cliente falhará
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", token); 
            response.put("token_type", "bearer");
            response.put("tipo", usuarioAutenticado.getTipo().name()); 

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("id", finalId); // Retorna o ID Relacional (Long) para o Gerente!
            userInfo.put("login", usuarioAutenticado.getLogin());
            response.put("user", userInfo);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Falha: Monta a mensagem de erro
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Login ou senha inválidos.");
            // Retorna 401 Unauthorized com a mensagem de erro
            return new ResponseEntity<>(erro, HttpStatus.UNAUTHORIZED);
        }
    }// end loginAuthh
    
    @PostMapping("/criarLogin")
    public AuthDTO criarLogin(@RequestBody ClienteAprovadoEvent evento) {        
        String senhaAleatoria = passwordService.generateRandomPassword();
        UserAuth novoUsuario = authService.createNewUser(evento.getEmail(), senhaAleatoria, UserAuth.TipoCliente.USUARIO);
        AuthDTO retorno = new AuthDTO();
        retorno.setLogin(novoUsuario.getLogin());
        retorno.setSenha(senhaAleatoria);
        
        return retorno;
    }
    


}
