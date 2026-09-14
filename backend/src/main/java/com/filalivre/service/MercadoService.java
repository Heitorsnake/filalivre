package com.filalivre.service;

import com.filalivre.dto.EntrarMercadoRequest;
import com.filalivre.dto.MercadoRequest;
import com.filalivre.dto.MercadoResponse;
import com.filalivre.model.Mercado;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MercadoService {

    private final MercadoRepository mercadoRepository;
    private final UsuarioRepository usuarioRepository;

    public MercadoService(MercadoRepository mercadoRepository, UsuarioRepository usuarioRepository) {
        this.mercadoRepository = mercadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MercadoResponse criar(MercadoRequest req, Usuario gestor) {
        exigirGestor(gestor);
        Mercado mercado = mercadoRepository.findByGestorId(gestor.getId()).orElseGet(Mercado::new);
        mercado.setNome(req.nome().trim());
        mercado.setGestor(gestor);
        if (mercado.getCodigoAcesso() == null) {
            mercado.setCodigoAcesso(novoCodigo());
        }
        mercado = mercadoRepository.save(mercado);
        gestor.setMercado(mercado);
        usuarioRepository.save(gestor);
        return toResponse(mercado);
    }

    @Transactional(readOnly = true)
    public MercadoResponse meuMercado(Usuario usuario) {
        return usuario.getMercado() == null ? null : toResponse(usuario.getMercado());
    }

    @Transactional
    public MercadoResponse entrar(EntrarMercadoRequest req, Usuario operador) {
        if (operador.getPerfil() != Perfil.OPERADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente operadores entram por código");
        }
        Mercado mercado = mercadoRepository.findByCodigoAcessoIgnoreCase(req.codigo().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Código do mercado inválido"));
        operador.setMercado(mercado);
        usuarioRepository.save(operador);
        return toResponse(mercado);
    }

    private void exigirGestor(Usuario usuario) {
        if (usuario.getPerfil() != Perfil.GERENTE && usuario.getPerfil() != Perfil.ADMINISTRADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas gestores podem cadastrar mercados");
        }
    }

    private String novoCodigo() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
        } while (mercadoRepository.findByCodigoAcessoIgnoreCase(codigo).isPresent());
        return codigo;
    }

    private MercadoResponse toResponse(Mercado mercado) {
        return new MercadoResponse(mercado.getId(), mercado.getNome(), mercado.getCodigoAcesso());
    }
}