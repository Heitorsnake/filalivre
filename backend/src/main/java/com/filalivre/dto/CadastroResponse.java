package com.filalivre.dto;

public record CadastroResponse(
        boolean verificacaoNecessaria,
        UsuarioResponse usuario,
        String mensagem) {
}