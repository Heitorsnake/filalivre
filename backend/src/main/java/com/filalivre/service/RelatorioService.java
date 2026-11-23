package com.filalivre.service;

import com.filalivre.dto.ResumoResponse;
import com.filalivre.dto.RelatorioDiaResponse;
import com.filalivre.dto.RelatorioSolicitacaoItemResponse;
import com.filalivre.dto.RelatorioSolicitacoesResponse;
import com.filalivre.model.Caixa;
import com.filalivre.model.Perfil;
import com.filalivre.model.StatusCaixa;
import com.filalivre.model.StatusSolicitacao;
import com.filalivre.model.Solicitacao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.time.Duration;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RelatorioService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CaixaRepository caixaRepository;

    public RelatorioService(SolicitacaoRepository solicitacaoRepository, CaixaRepository caixaRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.caixaRepository = caixaRepository;
    }

    public ResumoResponse resumo(Usuario usuario) {
        Long mercadoId = usuario.getMercadoId();
        boolean administrador = usuario.getPerfil() == Perfil.ADMINISTRADOR;
        List<Solicitacao> decididas = administrador
                ? solicitacaoRepository.findByStatusIn(
                        List.of(StatusSolicitacao.APROVADA, StatusSolicitacao.RECUSADA))
                : mercadoId == null ? List.of() : solicitacaoRepository
                        .findByCaixa_Mercado_IdAndStatusIn(mercadoId,
                                List.of(StatusSolicitacao.APROVADA, StatusSolicitacao.RECUSADA));

        double tempoMedioMinutos = decididas.stream()
                .filter(s -> s.getDecididoEm() != null)
                .mapToLong(s -> Duration.between(s.getCriadoEm(), s.getDecididoEm()).toMinutes())
                .average()
                .orElse(0.0);

        Map<String, Long> caixasPorStatus = new LinkedHashMap<>();
        for (StatusCaixa st : StatusCaixa.values()) {
            caixasPorStatus.put(st.name(), 0L);
        }
        if (administrador) {
            for (StatusCaixa st : StatusCaixa.values()) {
                caixasPorStatus.put(st.name(), caixaRepository.countByStatus(st));
            }
        } else if (mercadoId != null) {
            List<Caixa> caixas = caixaRepository.findAllByMercadoId(mercadoId);
            caixasPorStatus.putAll(caixas.stream()
                    .collect(Collectors.groupingBy(c -> c.getStatus().name(), Collectors.counting())));
        }

        long totalSolicitacoes = administrador
                ? solicitacaoRepository.count()
                : mercadoId == null ? 0 : solicitacaoRepository.countByCaixa_Mercado_Id(mercadoId);
        long pendentes = administrador
                ? solicitacaoRepository.countByStatus(StatusSolicitacao.PENDENTE)
                : mercadoId == null ? 0
                        : solicitacaoRepository.countByCaixa_Mercado_IdAndStatus(
                                mercadoId, StatusSolicitacao.PENDENTE);

        return new ResumoResponse(
                totalSolicitacoes,
                decididas.stream().filter(s -> s.getStatus() == StatusSolicitacao.APROVADA).count(),
                decididas.stream().filter(s -> s.getStatus() == StatusSolicitacao.RECUSADA).count(),
                pendentes,
                Math.round(tempoMedioMinutos * 10.0) / 10.0,
                LocalDateTime.now(),
                caixasPorStatus);
    }

    @Transactional(readOnly = true)
    public RelatorioSolicitacoesResponse solicitacoes(
            Usuario usuario, LocalDate inicio, LocalDate fim, StatusSolicitacao status) {
        LocalDate dataFim = fim == null ? LocalDate.now() : fim;
        LocalDate dataInicio = inicio == null ? dataFim.minusDays(29) : inicio;
        if (dataInicio.isAfter(dataFim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data inicial não pode ser posterior à data final");
        }
        if (ChronoUnit.DAYS.between(dataInicio, dataFim) > 365) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O período máximo do relatório é de 366 dias");
        }
        LocalDateTime fimExclusivo;
        try {
            fimExclusivo = dataFim.plusDays(1).atStartOfDay();
        } catch (DateTimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data final inválida");
        }

        Long mercadoId = usuario.getPerfil() == Perfil.ADMINISTRADOR ? null : usuario.getMercadoId();
        List<Solicitacao> registros = mercadoId == null && usuario.getPerfil() != Perfil.ADMINISTRADOR
                ? List.of()
                : solicitacaoRepository.buscarParaRelatorio(
                        mercadoId, dataInicio.atStartOfDay(), fimExclusivo, status);

        long pendentes = contarPorStatus(registros, StatusSolicitacao.PENDENTE);
        long aprovadas = contarPorStatus(registros, StatusSolicitacao.APROVADA);
        long recusadas = contarPorStatus(registros, StatusSolicitacao.RECUSADA);
        long decididas = aprovadas + recusadas;
        double percentualAprovacao = decididas == 0 ? 0.0 : aprovadas * 100.0 / decididas;
        double tempoMedioMinutos = registros.stream()
                .filter(s -> s.getDecididoEm() != null)
                .mapToLong(s -> Duration.between(s.getCriadoEm(), s.getDecididoEm()).toSeconds())
                .average()
                .orElse(0.0) / 60.0;

        Map<LocalDate, ContagemDia> contagens = new LinkedHashMap<>();
        for (Solicitacao registro : registros) {
            LocalDate data = registro.getCriadoEm().toLocalDate();
            ContagemDia contagem = contagens.computeIfAbsent(data, ignorada -> new ContagemDia());
            contagem.total++;
            switch (registro.getStatus()) {
                case PENDENTE -> contagem.pendentes++;
                case APROVADA -> contagem.aprovadas++;
                case RECUSADA -> contagem.recusadas++;
            }
        }

        List<RelatorioDiaResponse> porDia = new ArrayList<>();
        for (LocalDate data = dataInicio; !data.isAfter(dataFim); data = data.plusDays(1)) {
            ContagemDia contagem = contagens.getOrDefault(data, new ContagemDia());
            porDia.add(new RelatorioDiaResponse(
                    data, contagem.total, contagem.pendentes, contagem.aprovadas, contagem.recusadas));
        }
        List<RelatorioSolicitacaoItemResponse> itens = registros.stream()
                .map(s -> new RelatorioSolicitacaoItemResponse(
                        s.getId(),
                        s.getCaixa().getNumero(),
                        s.getOperador().getNome(),
                        s.getTipo(),
                        s.getProduto(),
                        s.getQuantidade(),
                        s.getValor(),
                        s.getStatus(),
                        s.getCriadoEm(),
                        s.getDecididoEm()))
                .toList();

        return new RelatorioSolicitacoesResponse(
                dataInicio,
                dataFim,
                status,
                LocalDateTime.now(),
                registros.size(),
                pendentes,
                aprovadas,
                recusadas,
                arredondar(percentualAprovacao),
                arredondar(tempoMedioMinutos),
                porDia,
                itens);
    }

    private long contarPorStatus(List<Solicitacao> solicitacoes, StatusSolicitacao status) {
        return solicitacoes.stream().filter(s -> s.getStatus() == status).count();
    }

    private double arredondar(double valor) {
        return Math.round(valor * 10.0) / 10.0;
    }

    private static class ContagemDia {
        private long total;
        private long pendentes;
        private long aprovadas;
        private long recusadas;
    }

}
