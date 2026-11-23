package com.filalivre.controller;

import com.filalivre.dto.ResumoResponse;
import com.filalivre.dto.RelatorioSolicitacoesResponse;
import com.filalivre.model.StatusSolicitacao;
import com.filalivre.service.RelatorioService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/solicitacoes")
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE', 'ADMINISTRADOR')")
    public RelatorioSolicitacoesResponse solicitacoes(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) StatusSolicitacao status) {
        return relatorioService.solicitacoes(usuario, inicio, fim, status);
    }
}
