package com.filalivre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CaixaRequest(@NotBlank @Size(max = 150) String localizacao) {
}
