package br.ufpr.bantads.ms_auth.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.ufpr.bantads.ms_auth.model.UserAuth;
import br.ufpr.bantads.ms_auth.service.AuthService;

@RestController
@RequestMapping("/api/auth/")

public class AuthController {
    
    @Autowired // injecção de dependência
    private AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> loginAuth(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserAuth usuarioAutenticado = authService.authenticate(loginRequestDTO.getLogin(), loginRequestDTO.getSenha());
        if (usuarioAutenticado != null) {
            
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", "PLACEHOLDER_JWT_TOKEN"); 
            response.put("token_type", "bearer");
            response.put("tipo", usuarioAutenticado.getTipo().name()); // Pega o nome do enum
            response.put("usuario", usuarioAutenticado); // Inclui os dados do usuário

            // Retorna 200 OK com o corpo da resposta
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Falha: Monta a mensagem de erro
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Login ou senha inválidos.");
            // Retorna 401 Unauthorized com a mensagem de erro
            return new ResponseEntity<>(erro, HttpStatus.UNAUTHORIZED);
        }
    }

}
