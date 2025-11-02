package br.ufpr.bantads.ms_auth.controller;

import lombok.Data;

@Data // anotação do Lombok para gerar getters e setters automaticamente
public class LoginRequestDTO {
    private String login;
    private String senha;
}
