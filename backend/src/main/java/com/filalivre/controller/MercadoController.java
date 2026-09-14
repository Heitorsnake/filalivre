package com.filalivre.controller;

import com.filalivre.dto.EntrarMercadoRequest;
import com.filalivre.dto.MercadoRequest;
import com.filalivre.dto.MercadoResponse;
import com.filalivre.model.Usuario;
import com.filalivre.service.MercadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mercados")
public class MercadoController {

    private final MercadoService mercadoService;

    public MercadoController(MercadoService mercadoService) { this.mercadoService = mercadoService; }

    @GetMapping("/meu")
    public MercadoResponse meu(@AuthenticationPrincipal Usuario usuario) {
        return mercadoService.meuMercado(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MercadoResponse criar(@Valid @RequestBody MercadoRequest req,
                                 @AuthenticationPrincipal Usuario usuario) {
        return mercadoService.criar(req, usuario);
    }

    @PostMapping("/entrar")
    public MercadoResponse entrar(@Valid @RequestBody EntrarMercadoRequest req,
                                  @AuthenticationPrincipal Usuario usuario) {
        return mercadoService.entrar(req, usuario);
    }
}