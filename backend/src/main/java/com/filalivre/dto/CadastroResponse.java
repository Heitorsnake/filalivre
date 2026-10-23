package com.filalivre.dto;

public record CadastroResponse(
        UsuarioResponse usuario,
        String mensagem) {
}