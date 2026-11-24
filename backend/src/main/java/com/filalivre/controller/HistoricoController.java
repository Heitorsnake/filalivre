package com.filalivre.controller;

import com.filalivre.dto.RegistroAcaoResponse;
import com.filalivre.model.Perfil;
import com.filalivre.model.RegistroAcao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.RegistroAcaoRepository;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {

    private final RegistroAcaoRepository registroAcaoRepository;
    private final CaixaRepository caixaRepository;

    public HistoricoController(
            RegistroAcaoRepository registroAcaoRepository,
            CaixaRepository caixaRepository) {
        this.registroAcaoRepository = registroAcaoRepository;
        this.caixaRepository = caixaRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('SUPERVISOR', 'GERENTE', 'ADMINISTRADOR')")
    public List<RegistroAcaoResponse> listar(@AuthenticationPrincipal Usuario usuario) {
        List<RegistroAcao> registros;
        if (usuario.getPerfil() == Perfil.SUPERVISOR) {
            Long mercadoId = usuario.getMercadoId();
            if (mercadoId == null) {
                return List.of();
            }
            List<Integer> numerosCaixas = caixaRepository.findAllByMercadoId(mercadoId).stream()
                    .map(caixa -> caixa.getNumero())
                    .toList();
            if (numerosCaixas.isEmpty()) {
                return List.of();
            }
            registros = registroAcaoRepository.findTop100ByCaixaNumeroInOrderByMomentoDesc(numerosCaixas);
        } else {
            registros = registroAcaoRepository.findTop100ByOrderByMomentoDesc();
        }
        return registros.stream()
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
