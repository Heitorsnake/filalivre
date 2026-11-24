package com.filalivre.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void erroInternoRetornaMensagemGenericaSemDetalhesDaExcecao() {
        String detalheInterno = "internal-test-marker";

        ResponseEntity<Map<String, String>> resposta = handler.erroGeral(
                new IllegalStateException(detalheInterno));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.getStatusCode());
        var body = resposta.getBody();
        assertNotNull(body);
        assertEquals("Erro interno do servidor", body.get("erro"));
        assertFalse(body.toString().contains(detalheInterno));
    }
}
