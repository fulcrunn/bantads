package br.ufpr.bantads.ms_auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service // anotação para definir que essa classe é um serviço
public class PasswordService {

        
    public byte[] hashPassword(String senha, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Converte a senha para bytes
        byte[] senhaBytes = senha.getBytes(StandardCharsets.UTF_8);
        // Concatena a senha com o salt
        byte[] senhaComSalt = new byte[senhaBytes.length + salt.length];
        // Copia o senhaBytes para um novo array
        System.arraycopy(senhaBytes, 0, senhaComSalt, 0, senhaBytes.length);
        // Junta nesse novo array o salt
        System.arraycopy(salt, 0, senhaComSalt, senhaBytes.length, salt.length);
        // Cria o hash usando SHA-256
        MessageDigest intanciaSHA = MessageDigest.getInstance("SHA-256");
        byte [] senhaComSHA = intanciaSHA.digest(senhaComSalt);
        return senhaComSHA;
    }

    public boolean verifyPassword(String senhaDigitada, byte[] hashArmazenado, byte[] saltArmazenado) {
        try {
            byte[] hashDigitado = hashPassword(senhaDigitada, saltArmazenado);            
            return Arrays.equals(hashArmazenado, hashDigitado);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateRandomPassword(){
        String maiusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        String simbolos = "!@#$%^&*()-_+=<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder senhaBuilder = new StringBuilder();
        // Gera o meu vetor senha com pelo menos um caractere de cada tipo
        senhaBuilder.append(maiusculas.charAt(random.nextInt(maiusculas.length())));
        senhaBuilder.append(minusculas.charAt(random.nextInt(minusculas.length())));
        senhaBuilder.append(numeros.charAt(random.nextInt(numeros.length())));
        senhaBuilder.append(simbolos.charAt(random.nextInt(simbolos.length())));

        String todosCaracteres = maiusculas + minusculas + numeros + simbolos;

        // Preenche o restante da senha com caracteres aleatórios do conjunto completo
        for (int i = 4; i < 8; i++) {
            senhaBuilder.append(todosCaracteres.charAt(random.nextInt(todosCaracteres.length())));
        }

        // Embaralha a senha para evitar padrões previsíveis
        List<Character> listaDeCaracteres = IntStream.range(0, senhaBuilder.length())
                .mapToObj(senhaBuilder::charAt)
                .collect(Collectors.toList());
        Collections.shuffle(listaDeCaracteres, random);

        String senhaEmbaraleada = listaDeCaracteres.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return senhaEmbaraleada;
    }

    static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}
