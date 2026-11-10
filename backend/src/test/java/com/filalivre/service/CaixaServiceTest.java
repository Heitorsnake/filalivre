package com.filalivre.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.filalivre.dto.TotaisRequest;
import com.filalivre.model.Caixa;
import com.filalivre.model.Mercado;
import com.filalivre.model.StatusCaixa;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CaixaServiceTest {

    private static final Long CAIXA_ID = 5L;

    @Mock
    private CaixaRepository caixaRepository;

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private CaixaService caixaService;

    private Caixa caixa;
    private Usuario operador;
    private Usuario responsavel;

    @BeforeEach
    void configurarCaixaNoMercadoDoOperador() {
        Mercado mercado = org.mockito.Mockito.mock(Mercado.class);
        caixa = new Caixa();
        operador = org.mockito.Mockito.mock(Usuario.class);
        responsavel = org.mockito.Mockito.mock(Usuario.class);

        when(operador.pertenceAoMercado(mercado)).thenReturn(true);
        caixa.setMercado(mercado);
        when(caixaRepository.findByIdAndAtivoTrue(CAIXA_ID)).thenReturn(Optional.of(caixa));
    }

    @Test
    void operadorNaoPodeAlterarAtendimentoDeOutroOperador() {
        when(operador.getId()).thenReturn(2L);
        when(responsavel.getId()).thenReturn(1L);
        caixa.setOperador(responsavel);

        assertAll(
                () -> assertForbidden(() -> caixaService.alternarEspera(CAIXA_ID, operador)),
                () -> assertForbidden(() -> caixaService.finalizarAtendimento(CAIXA_ID, operador)),
                () -> assertForbidden(() -> caixaService.atualizarTotais(
                        CAIXA_ID, operador, new TotaisRequest(BigDecimal.ONE, 1))));

        assertEquals(StatusCaixa.NORMAL, caixa.getStatus());
        assertEquals(responsavel, caixa.getOperador());
        assertEquals(BigDecimal.ZERO, caixa.getValorCompra());
    }

    @Test
    void caixaOcupadoNaoPodeReceberNovoAtendimento() {
        caixa.setOperador(responsavel);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> caixaService.iniciarAtendimento(CAIXA_ID, operador));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(responsavel, caixa.getOperador());
    }

    private void assertForbidden(org.junit.jupiter.api.function.Executable operacao) {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, operacao);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }
}
