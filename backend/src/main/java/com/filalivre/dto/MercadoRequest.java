package com.filalivre.dto;

import jakarta.validation.constraints.NotBlank;

public record MercadoRequest(@NotBlank String nome) {}