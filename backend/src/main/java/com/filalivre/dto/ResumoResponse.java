package com.filalivre.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ResumoResponse(
        long totalSolicitacoes,
        long aprovadas,
        long recusadas,
        long pendentes,
        double tempoMedioDecisaoMinutos,
        LocalDateTime geradoEm,
        Map<String, Long> caixasPorStatus) {
}
