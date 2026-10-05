package com.filalivre.service;

import com.filalivre.dto.CadastroRequest;
import com.filalivre.dto.CadastroResponse;
import com.filalivre.dto.LoginRequest;
import com.filalivre.dto.UsuarioResponse;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.UsuarioRepository;
import java.time.Instant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;
    private final EmailVerificacaoService emailVerificacaoService;

    public AuthService(UsuarioRepository usuarioRepository,
                       AuthenticationManager authenticationManager,
                       SecurityContextRepository securityContextRepository,
                       PasswordEncoder passwordEncoder,
                       AuditoriaService auditoriaService,
                       EmailVerificacaoService emailVerificacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
        this.emailVerificacaoService = emailVerificacaoService;
    }

    @Transactional
    public CadastroResponse cadastrar(CadastroRequest req, HttpServletRequest request, HttpServletResponse response) {
        String email = req.email().trim().toLowerCase(java.util.Locale.ROOT);
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(req.senha()));
        try {
            Perfil perfil = Perfil.valueOf(req.perfil().trim().toUpperCase());
            if (perfil != Perfil.OPERADOR && perfil != Perfil.SUPERVISOR) {
                throw new IllegalArgumentException();
            }
            usuario.setPerfil(perfil);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Escolha Operador ou Supervisor");
        }

        if (emailVerificacaoService.requerVerificacao(email)) {
            String token = emailVerificacaoService.prepararToken(usuario);
            usuario = usuarioRepository.save(usuario);
            emailVerificacaoService.enviar(usuario.getNome(), email, token);
            return new CadastroResponse(true, null,
                    "Enviamos um código de 6 dígitos para seu Gmail. Digite-o para ativar sua conta.");
        }

        usuario = usuarioRepository.save(usuario);

        autenticarESalvarSessao(new LoginRequest(usuario.getEmail(), req.senha()), request, response);
        return new CadastroResponse(false, UsuarioResponse.de(usuario), "Conta criada.");
    }

    @Transactional
    public boolean verificarEmail(String emailInformado, String codigo) {
        String email = emailInformado.trim().toLowerCase(java.util.Locale.ROOT);
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);
        if (usuario == null || usuario.isEmailVerificado() || !emailVerificacaoService.requerVerificacao(email)) {
            return false;
        }
        if (usuario.getTentativasVerificacaoEmail() >= 5) {
            usuario.setTokenVerificacaoEmailHash(null);
            usuario.setTokenVerificacaoEmailExpiraEm(null);
            usuarioRepository.save(usuario);
            return false;
        }
        if (usuario.getTokenVerificacaoEmailExpiraEm() == null
                || !usuario.getTokenVerificacaoEmailExpiraEm().isAfter(Instant.now())) {
            usuario.setTokenVerificacaoEmailHash(null);
            usuario.setTokenVerificacaoEmailExpiraEm(null);
            usuarioRepository.save(usuario);
            return false;
        }
        byte[] codigoRecebido = emailVerificacaoService.hashToken(codigo)
                .getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] codigoEsperado = usuario.getTokenVerificacaoEmailHash()
                .getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (!java.security.MessageDigest.isEqual(codigoEsperado, codigoRecebido)) {
            int tentativas = usuario.getTentativasVerificacaoEmail() + 1;
            usuario.setTentativasVerificacaoEmail(tentativas);
            if (tentativas >= 5) {
                usuario.setTokenVerificacaoEmailHash(null);
                usuario.setTokenVerificacaoEmailExpiraEm(null);
            }
            usuarioRepository.save(usuario);
            return false;
        }
        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacaoEmailHash(null);
        usuario.setTokenVerificacaoEmailExpiraEm(null);
        usuario.setTentativasVerificacaoEmail(0);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(usuario, null, "EMAIL_VERIFICADO", "Endereço Gmail confirmado");
        return true;
    }

    @Transactional
    public void reenviarVerificacao(String emailInformado) {
        String email = emailInformado.trim().toLowerCase(java.util.Locale.ROOT);
        usuarioRepository.findByEmailIgnoreCase(email).ifPresent(usuario -> {
            if (!usuario.isEmailVerificado()
                    && emailVerificacaoService.requerVerificacao(email)
                    && emailVerificacaoService.reservarReenvio(email)) {
                String token = emailVerificacaoService.prepararToken(usuario);
                usuarioRepository.save(usuario);
                emailVerificacaoService.enviar(usuario.getNome(), email, token);
            }
        });
    }

    public UsuarioResponse login(LoginRequest req, HttpServletRequest request, HttpServletResponse response) {
        return autenticarESalvarSessao(
            new LoginRequest(req.email().trim().toLowerCase(java.util.Locale.ROOT), req.senha()), request, response);
    }

    private UsuarioResponse autenticarESalvarSessao(LoginRequest req, HttpServletRequest request,
                                                    HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(req.email(), req.senha()));
            if (request.getSession(false) != null) {
                request.changeSessionId();
            }
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            Usuario usuario = (Usuario) authentication.getPrincipal();
            auditoriaService.registrar(usuario, null, "LOGIN", "Usuário autenticado");
            return UsuarioResponse.de(usuario);
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
        }
    }
}
