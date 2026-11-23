package com.filalivre.dto;

import com.filalivre.model.StatusSolicitacao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RelatorioSolicitacoesResponse(
        LocalDate inicio,
        LocalDate fim,
        StatusSolicitacao status,
        LocalDateTime geradoEm,
        long total,
        long pendentes,
        long aprovadas,
        long recusadas,
        double percentualAprovacao,
        double tempoMedioDecisaoMinutos,
        List<RelatorioDiaResponse> porDia,
        List<RelatorioSolicitacaoItemResponse> solicitacoes) {
}
