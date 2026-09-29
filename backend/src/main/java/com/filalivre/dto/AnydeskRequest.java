package com.filalivre.dto;

import jakarta.validation.constraints.Size;

public record AnydeskRequest(@Size(max = 64) String anydeskId) {
}