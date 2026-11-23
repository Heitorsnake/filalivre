package com.filalivre.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.filalivre.model.Caixa;
import com.filalivre.model.Perfil;
import com.filalivre.model.Solicitacao;
import com.filalivre.model.StatusCaixa;
import com.filalivre.model.StatusSolicitacao;
import com.filalivre.model.TipoSolicitacao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Test
    void relatorioFiltraPorMercadoPeriodoEStatusECalculaIndicadoresReais() {
        Usuario usuario = Mockito.mock(Usuario.class);
        when(usuario.getPerfil()).thenReturn(Perfil.SUPERVISOR);
        when(usuario.getMercadoId()).thenReturn(42L);
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fim = LocalDate.of(2025, 1, 2);
        LocalDateTime criadoEm = inicio.atTime(10, 0);
        LocalDateTime decididoEm = criadoEm.plusMinutes(5);
        Solicitacao solicitacao = Mockito.mock(Solicitacao.class);
        Caixa caixa = Mockito.mock(Caixa.class);
        Usuario operador = Mockito.mock(Usuario.class);
        when(solicitacaoRepository.buscarParaRelatorio(
                eq(42L), eq(inicio.atStartOfDay()), eq(fim.plusDays(1).atStartOfDay()),
                eq(StatusSolicitacao.APROVADA))).thenReturn(List.of(solicitacao));
        when(solicitacao.getStatus()).thenReturn(StatusSolicitacao.APROVADA);
        when(solicitacao.getCriadoEm()).thenReturn(criadoEm);
        when(solicitacao.getDecididoEm()).thenReturn(decididoEm);
        when(solicitacao.getCaixa()).thenReturn(caixa);
        when(caixa.getNumero()).thenReturn(7);
        when(solicitacao.getOperador()).thenReturn(operador);
        when(operador.getNome()).thenReturn("Operador");
        when(solicitacao.getTipo()).thenReturn(TipoSolicitacao.CANCELAMENTO);
        when(solicitacao.getProduto()).thenReturn("Arroz");
        when(solicitacao.getQuantidade()).thenReturn(2);
        when(solicitacao.getValor()).thenReturn(new BigDecimal("12.50"));

        var relatorio = relatorioService.solicitacoes(
                usuario, inicio, fim, StatusSolicitacao.APROVADA);

        assertEquals(1L, relatorio.total());
        assertEquals(1L, relatorio.aprovadas());
        assertEquals(100.0, relatorio.percentualAprovacao());
        assertEquals(5.0, relatorio.tempoMedioDecisaoMinutos());
        assertEquals(2, relatorio.porDia().size());
        assertEquals(1L, relatorio.porDia().get(0).aprovadas());
        assertEquals(0L, relatorio.porDia().get(1).total());
        assertEquals("Arroz", relatorio.solicitacoes().get(0).produto());
        verify(solicitacaoRepository).buscarParaRelatorio(
                42L, inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay(),
                StatusSolicitacao.APROVADA);
        verifyNoMoreInteractions(solicitacaoRepository);
    }

    @Test
    void relatorioRejeitaPeriodoInvalidoAntesDeConsultarDados() {
        Usuario usuario = Mockito.mock(Usuario.class);
        LocalDate inicio = LocalDate.of(2025, 1, 2);
        LocalDate fim = LocalDate.of(2025, 1, 1);

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> relatorioService.solicitacoes(usuario, inicio, fim, null));

        verifyNoInteractions(solicitacaoRepository, caixaRepository);
    }

    @Test
    void resumoDoAdministradorUsaIndicadoresGlobaisDoBanco() {
        Usuario administrador = Mockito.mock(Usuario.class);
        Solicitacao aprovada = Mockito.mock(Solicitacao.class);
        Solicitacao recusada = Mockito.mock(Solicitacao.class);
        when(administrador.getPerfil()).thenReturn(Perfil.ADMINISTRADOR);
        when(administrador.getMercadoId()).thenReturn(null);
        LocalDateTime criadoAprovada = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime criadoRecusada = LocalDateTime.of(2025, 1, 1, 11, 0);
        when(aprovada.getStatus()).thenReturn(StatusSolicitacao.APROVADA);
        when(aprovada.getCriadoEm()).thenReturn(criadoAprovada);
        when(aprovada.getDecididoEm()).thenReturn(criadoAprovada.plusMinutes(10));
        when(recusada.getStatus()).thenReturn(StatusSolicitacao.RECUSADA);
        when(recusada.getCriadoEm()).thenReturn(criadoRecusada);
        when(recusada.getDecididoEm()).thenReturn(criadoRecusada.plusMinutes(20));
        when(solicitacaoRepository.findByStatusIn(
                List.of(StatusSolicitacao.APROVADA, StatusSolicitacao.RECUSADA)))
                .thenReturn(List.of(aprovada, recusada));
        when(solicitacaoRepository.count()).thenReturn(8L);
        when(solicitacaoRepository.countByStatus(StatusSolicitacao.PENDENTE)).thenReturn(3L);
        when(caixaRepository.countByStatus(StatusCaixa.NORMAL)).thenReturn(2L);
        when(caixaRepository.countByStatus(StatusCaixa.AGUARDANDO)).thenReturn(1L);
        when(caixaRepository.countByStatus(StatusCaixa.SOLICITACAO)).thenReturn(0L);
        when(caixaRepository.countByStatus(StatusCaixa.APROVACAO)).thenReturn(0L);

        var resumo = relatorioService.resumo(administrador);

        assertEquals(8L, resumo.totalSolicitacoes());
        assertEquals(1L, resumo.aprovadas());
        assertEquals(1L, resumo.recusadas());
        assertEquals(3L, resumo.pendentes());
        assertEquals(15.0, resumo.tempoMedioDecisaoMinutos());
        assertEquals(2L, resumo.caixasPorStatus().get(StatusCaixa.NORMAL.name()));
        assertEquals(1L, resumo.caixasPorStatus().get(StatusCaixa.AGUARDANDO.name()));
        verify(solicitacaoRepository).count();
        verify(solicitacaoRepository).countByStatus(StatusSolicitacao.PENDENTE);
        verify(caixaRepository).countByStatus(StatusCaixa.NORMAL);
        verify(caixaRepository).countByStatus(StatusCaixa.AGUARDANDO);
        verify(caixaRepository).countByStatus(StatusCaixa.SOLICITACAO);
        verify(caixaRepository).countByStatus(StatusCaixa.APROVACAO);
        verify(solicitacaoRepository, never()).countByCaixa_Mercado_Id(42L);
        verify(caixaRepository, never()).findAllByMercadoId(42L);
    }
}
