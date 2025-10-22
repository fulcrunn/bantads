package br.ufpr.bantads.ms_auth.service;


import java.util.Optional;
import org.springframework.stereotype.Service;
import br.ufpr.bantads.ms_auth.repository.AuthRepository;
import br.ufpr.bantads.ms_auth.model.UserAuth;

@Service // anotação para definir que essa classe é um serviço
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordService passwordService;

    public AuthService(AuthRepository authRepository, PasswordService passwordService) {
        this.authRepository = authRepository;
        this.passwordService = passwordService;
    }
    
    public Optional<UserAuth> findByLogin(String login) {
        return authRepository.findByLogin(login);
    }
    
    public UserAuth authenticate(String login, String password) {
        Optional<UserAuth> userAuthOpt = authRepository.findByLogin(login);
        if (userAuthOpt.isPresent()) {
            UserAuth userAuth = userAuthOpt.get();
            // Directly use the boolean result from verifyPassword
            if (passwordService.verifyPassword(password, userAuth.getHashSenha(), userAuth.getSalt())) {
                return userAuth;
            }
        }
        return null; // Return null if user not found or password incorrect
    }
}
