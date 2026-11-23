package com.filalivre.dto;

import java.time.LocalDate;

public record RelatorioDiaResponse(
        LocalDate data,
        long total,
        long pendentes,
        long aprovadas,
        long recusadas) {
}
