package com.filalivre.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.filalivre.model.TipoSolicitacao;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RequisicoesValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void codigoDeMercadoRespeitaLimiteDoBanco() {
        assertFalse(validator.validate(new EntrarMercadoRequest("A".repeat(12))).stream()
                .anyMatch(erro -> erro.getPropertyPath().toString().equals("codigo")));
        assertTrue(validator.validate(new EntrarMercadoRequest("A".repeat(13))).stream()
                .anyMatch(erro -> erro.getPropertyPath().toString().equals("codigo")));
    }

    @Test
    void solicitacaoRejeitaProdutoEmBrancoEValoresNaoPositivos() {
        SolicitacaoRequest invalida = new SolicitacaoRequest(
                1L, TipoSolicitacao.CANCELAMENTO, " ", 0, BigDecimal.ZERO, null);

        var camposInvalidos = validator.validate(invalida).stream()
                .map(erro -> erro.getPropertyPath().toString())
                .toList();

        assertTrue(camposInvalidos.contains("produto"));
        assertTrue(camposInvalidos.contains("quantidade"));
        assertTrue(camposInvalidos.contains("valor"));
    }
}
