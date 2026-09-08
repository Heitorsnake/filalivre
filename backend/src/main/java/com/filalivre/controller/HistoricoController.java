package com.filalivre.controller;

import com.filalivre.dto.RegistroAcaoResponse;
import com.filalivre.model.RegistroAcao;
import com.filalivre.repository.RegistroAcaoRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    private final RegistroAcaoRepository registroAcaoRepository;

    public HistoricoController(RegistroAcaoRepository registroAcaoRepository) {
        this.registroAcaoRepository = registroAcaoRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMINISTRADOR')")
    public List<RegistroAcaoResponse> listar() {
        return registroAcaoRepository.findTop100ByOrderByMomentoDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    private RegistroAcaoResponse toResponse(RegistroAcao r) {
        return new RegistroAcaoResponse(
                r.getId(),
                r.getUsuario().getNome(),
                r.getCaixaNumero(),
                r.getAcao(),
                r.getDetalhes(),
                r.getMomento());
    }
}
