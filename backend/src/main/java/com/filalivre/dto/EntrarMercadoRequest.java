package com.filalivre.dto;

import jakarta.validation.constraints.NotBlank;

public record EntrarMercadoRequest(@NotBlank String codigo) {}