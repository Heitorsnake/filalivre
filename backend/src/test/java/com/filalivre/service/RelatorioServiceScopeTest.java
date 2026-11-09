package com.filalivre.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.filalivre.model.StatusSolicitacao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceScopeTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private CaixaRepository caixaRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    void resumoConsultaSomenteDadosDoMercadoDoUsuario() {
        Usuario usuario = Mockito.mock(Usuario.class);
        when(usuario.getMercadoId()).thenReturn(42L);
        when(solicitacaoRepository.findByCaixa_Mercado_IdAndStatusIn(eq(42L), anyCollection()))
                .thenReturn(List.of());
        when(solicitacaoRepository.countByCaixa_Mercado_Id(42L)).thenReturn(3L);
        when(solicitacaoRepository.countByCaixa_Mercado_IdAndStatus(42L, StatusSolicitacao.PENDENTE))
                .thenReturn(1L);
        when(caixaRepository.findAllByMercadoId(42L)).thenReturn(List.of());

        var resumo = relatorioService.resumo(usuario);
        assertNotNull(resumo);
        assertEquals(3L, resumo.totalSolicitacoes());
        assertEquals(1L, resumo.pendentes());
        assertEquals(0.0, resumo.tempoMedioDecisaoMinutos());

        verify(solicitacaoRepository).findByCaixa_Mercado_IdAndStatusIn(eq(42L), anyCollection());
        verify(solicitacaoRepository).countByCaixa_Mercado_Id(42L);
        verify(solicitacaoRepository).countByCaixa_Mercado_IdAndStatus(42L, StatusSolicitacao.PENDENTE);
        verify(caixaRepository).findAllByMercadoId(42L);
        verify(solicitacaoRepository, never()).findAll();
        verify(caixaRepository, never()).findAll();
    }

    @Test
    void usuarioSemMercadoRecebeResumoVazioSemConsultarRepositorios() {
        Usuario usuario = Mockito.mock(Usuario.class);
        when(usuario.getMercadoId()).thenReturn(null);

        var resumo = relatorioService.resumo(usuario);

        assertEquals(0L, resumo.totalSolicitacoes());
        assertEquals(0L, resumo.pendentes());
        verifyNoInteractions(solicitacaoRepository, caixaRepository);
    }
}
