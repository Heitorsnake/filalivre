package com.filalivre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EntrarMercadoRequest(@NotBlank @Size(max = 12) String codigo) {}