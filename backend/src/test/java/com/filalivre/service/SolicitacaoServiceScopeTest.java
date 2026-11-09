package com.filalivre.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
class SolicitacaoServiceScopeTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private CaixaRepository caixaRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Test
    void listaPendenciasEHistoricoUsandoOMercadoDoGestor() {
        Usuario gestor = Mockito.mock(Usuario.class);
        when(gestor.getMercadoId()).thenReturn(42L);
        when(solicitacaoRepository.findByCaixa_Mercado_IdAndStatusOrderByCriadoEmDesc(
                42L, StatusSolicitacao.PENDENTE)).thenReturn(List.of());
        when(solicitacaoRepository.findTop100ByCaixa_Mercado_IdOrderByCriadoEmDesc(42L))
                .thenReturn(List.of());

        assertTrue(solicitacaoService.pendentes(gestor).isEmpty());
        assertTrue(solicitacaoService.historico(gestor).isEmpty());

        verify(solicitacaoRepository).findByCaixa_Mercado_IdAndStatusOrderByCriadoEmDesc(
                42L, StatusSolicitacao.PENDENTE);
        verify(solicitacaoRepository).findTop100ByCaixa_Mercado_IdOrderByCriadoEmDesc(42L);
    }

    @Test
    void usuarioSemMercadoNaoConsultaSolicitacoesGlobais() {
        Usuario gestor = Mockito.mock(Usuario.class);
        when(gestor.getMercadoId()).thenReturn(null);

        assertTrue(solicitacaoService.pendentes(gestor).isEmpty());
        assertTrue(solicitacaoService.historico(gestor).isEmpty());

        verify(solicitacaoRepository, never()).findByCaixa_Mercado_IdAndStatusOrderByCriadoEmDesc(
                any(), any());
        verify(solicitacaoRepository, never()).findTop100ByCaixa_Mercado_IdOrderByCriadoEmDesc(any());
    }
}
