package com.filalivre.service;

import com.filalivre.dto.ResumoResponse;
import com.filalivre.model.Caixa;
import com.filalivre.model.StatusCaixa;
import com.filalivre.model.StatusSolicitacao;
import com.filalivre.model.Solicitacao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
        List<Solicitacao> decididas = mercadoId == null ? List.of() : solicitacaoRepository
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
        List<Caixa> caixas = mercadoId == null ? List.of() : caixaRepository.findAllByMercadoId(mercadoId);
        caixasPorStatus.putAll(caixas.stream()
            .collect(Collectors.groupingBy(c -> c.getStatus().name(), Collectors.counting())));

        long totalSolicitacoes = mercadoId == null ? 0 : solicitacaoRepository.countByCaixa_Mercado_Id(mercadoId);
        long pendentes = mercadoId == null ? 0
            : solicitacaoRepository.countByCaixa_Mercado_IdAndStatus(mercadoId, StatusSolicitacao.PENDENTE);

        return new ResumoResponse(
                totalSolicitacoes,
                decididas.stream().filter(s -> s.getStatus() == StatusSolicitacao.APROVADA).count(),
                decididas.stream().filter(s -> s.getStatus() == StatusSolicitacao.RECUSADA).count(),
                pendentes,
                Math.round(tempoMedioMinutos * 10.0) / 10.0,
                LocalDateTime.now(),
                caixasPorStatus);
    }

}
