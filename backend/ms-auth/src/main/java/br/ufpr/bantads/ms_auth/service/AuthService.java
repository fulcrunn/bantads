package br.ufpr.bantads.ms_auth.service;


import java.security.NoSuchAlgorithmException;
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

    public UserAuth createNewUser(String login, String senha, UserAuth.TipoCliente tipo) {
        byte[] salt;
        byte[] hashSenha;
        if(authRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Login já existe." + login);
        }
        try {
            salt = PasswordService.generateSalt();            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar salt da senha", e);
        }
        try {
            hashSenha = passwordService.hashPassword(senha, salt);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
        
        UserAuth novoUsuario = new UserAuth();
        novoUsuario.setLogin(login);
        novoUsuario.setSalt(salt);
        novoUsuario.setHashSenha(hashSenha);
        novoUsuario.setTipo(tipo);
        return authRepository.save(novoUsuario);
        
    }

    public UserAuth createUserWithRandomPassword(String login, UserAuth.TipoCliente tipo) throws NoSuchAlgorithmException {
        String senhaAleatoria = passwordService.generateRandomPassword();
        System.out.println("Senha gerada para " + login + ": " + senhaAleatoria); // Manter para debug inicial!
        // Deixa a NoSuchAlgorithmException ser propagada
        return createNewUser(login, senhaAleatoria, tipo);      
    }
}
