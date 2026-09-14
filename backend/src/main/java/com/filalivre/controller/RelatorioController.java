package com.filalivre.controller;

import com.filalivre.dto.ResumoResponse;
import com.filalivre.service.RelatorioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.filalivre.model.Usuario;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE', 'ADMINISTRADOR')")
    public ResumoResponse resumo(@AuthenticationPrincipal Usuario usuario) {
        return relatorioService.resumo(usuario);
    }
}
