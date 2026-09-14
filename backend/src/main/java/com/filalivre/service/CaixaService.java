package com.filalivre.service;

import com.filalivre.dto.CaixaRequest;
import com.filalivre.dto.CaixaResponse;
import com.filalivre.dto.TotaisRequest;
import com.filalivre.model.Caixa;
import com.filalivre.model.StatusCaixa;
import com.filalivre.model.StatusSolicitacao;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.SolicitacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final AuditoriaService auditoriaService;

    public CaixaService(CaixaRepository caixaRepository,
                        SolicitacaoRepository solicitacaoRepository,
                        AuditoriaService auditoriaService) {
        this.caixaRepository = caixaRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.auditoriaService = auditoriaService;
    }

    public List<CaixaResponse> listar(Usuario usuario) {
        if (usuario.getMercado() == null) return List.of();
        return caixaRepository.findAllByMercadoIdAndAtivoTrueOrderByNumeroAsc(usuario.getMercado().getId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CaixaResponse criar(CaixaRequest req, Usuario usuario) {
        if (usuario.getMercado() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastre um mercado antes de criar caixas");
        }
        Caixa caixa = new Caixa();
        Integer maiorNumero = caixaRepository.findTopByOrderByNumeroDesc()
            .map(Caixa::getNumero)
            .orElse(null);
        caixa.setNumero(maiorNumero == null ? 1 : maiorNumero + 1);
        caixa.setLocalizacao(req.localizacao().trim());
        caixa.setMercado(usuario.getMercado());
        caixa = caixaRepository.save(caixa);
        return toResponse(caixa);
    }

    @Transactional
    public CaixaResponse editar(Long id, CaixaRequest req, Usuario usuario) {
        Caixa caixa = buscar(id, usuario);
        caixa.setLocalizacao(req.localizacao().trim());
        auditoriaService.registrar(usuario, caixa.getNumero(), "CAIXA_EDITADO",
                "Localização alterada para " + caixa.getLocalizacao());
        return toResponse(caixa);
    }

    @Transactional
    public void desativar(Long id, Usuario usuario) {
        Caixa caixa = buscar(id, usuario);
        if (caixa.getOperador() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Finalize o atendimento antes de desativar o caixa");
        }
        caixa.setAtivo(false);
        auditoriaService.registrar(usuario, caixa.getNumero(), "CAIXA_DESATIVADO",
                "Caixa desativado; número preservado para auditoria");
    }

    @Transactional
    public CaixaResponse iniciarAtendimento(Long id, Usuario operador) {
        Caixa caixa = buscar(id, operador);
        caixa.setOperador(operador);
        caixa.setStatus(StatusCaixa.NORMAL);
        caixa.setValorCompra(BigDecimal.ZERO);
        caixa.setQtdItens(0);
        caixa.setInicioAtendimento(LocalDateTime.now());
        auditoriaService.registrar(operador, caixa.getNumero(), "INICIO_ATENDIMENTO",
                "Operador assumiu o caixa");
        return toResponse(caixa);
    }

    @Transactional
    public CaixaResponse alternarEspera(Long id, Usuario usuario) {
        Caixa caixa = buscar(id, usuario);
        if (caixa.getStatus() == StatusCaixa.SOLICITACAO || caixa.getStatus() == StatusCaixa.APROVACAO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível alterar o status: existe uma solicitação em andamento neste caixa");
        }
        if (caixa.getStatus() == StatusCaixa.NORMAL) {
            caixa.setStatus(StatusCaixa.AGUARDANDO);
            auditoriaService.registrar(usuario, caixa.getNumero(), "CAIXA_EM_ESPERA", "Caixa colocado em espera");
        } else {
            caixa.setStatus(StatusCaixa.NORMAL);
            auditoriaService.registrar(usuario, caixa.getNumero(), "ESPERA_REMOVIDA", "Caixa voltou ao normal");
        }
        return toResponse(caixa);
    }

    @Transactional
    public CaixaResponse finalizarAtendimento(Long id, Usuario usuario) {
        Caixa caixa = buscar(id, usuario);
        String detalhes = String.format("Compra finalizada: %d itens, R$ %.2f",
                caixa.getQtdItens(), caixa.getValorCompra());
        caixa.setOperador(null);
        caixa.setStatus(StatusCaixa.NORMAL);
        caixa.setValorCompra(BigDecimal.ZERO);
        caixa.setQtdItens(0);
        caixa.setInicioAtendimento(null);
        auditoriaService.registrar(usuario, caixa.getNumero(), "FIM_ATENDIMENTO", detalhes);
        return toResponse(caixa);
    }

    @Transactional
    public CaixaResponse atualizarTotais(Long id, Usuario operador, TotaisRequest req) {
        Caixa caixa = buscar(id, operador);
        caixa.setValorCompra(req.valorCompra());
        caixa.setQtdItens(req.qtdItens());
        return toResponse(caixa);
    }

    private Caixa buscar(Long id, Usuario usuario) {
        Caixa caixa = caixaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado"));
        if (usuario.getMercado() == null || caixa.getMercado() == null
                || !caixa.getMercado().getId().equals(usuario.getMercado().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caixa não encontrado");
        }
        return caixa;
    }

    private CaixaResponse toResponse(Caixa c) {
        boolean pendente = solicitacaoRepository.existsByCaixaIdAndStatus(c.getId(), StatusSolicitacao.PENDENTE);
        return new CaixaResponse(
                c.getId(),
                c.getNumero(),
                c.getLocalizacao(),
                c.isAtivo(),
                c.getStatus(),
                c.getValorCompra(),
                c.getQtdItens(),
                c.getOperador() != null ? c.getOperador().getNome() : null,
                c.getInicioAtendimento(),
                pendente);
    }
}
