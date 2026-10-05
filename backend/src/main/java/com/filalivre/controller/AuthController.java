package com.filalivre.controller;

import com.filalivre.dto.CadastroRequest;
import com.filalivre.dto.CadastroResponse;
import com.filalivre.dto.LoginRequest;
import com.filalivre.dto.ReenviarVerificacaoRequest;
import com.filalivre.dto.UsuarioResponse;
import com.filalivre.dto.VerificarEmailRequest;
import com.filalivre.model.Usuario;
import com.filalivre.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/csrf")
    public java.util.Map<String, String> csrf(CsrfToken csrfToken) {
        return java.util.Map.of("token", csrfToken.getToken());
    }

    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest req,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        return authService.login(req, request, response);
    }

    @PostMapping("/cadastro")
    public CadastroResponse cadastro(@Valid @RequestBody CadastroRequest req,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        return authService.cadastrar(req, request, response);
    }

    @PostMapping("/verificar-email")
    public java.util.Map<String, Object> verificarEmail(@Valid @RequestBody VerificarEmailRequest req) {
        boolean verificado = authService.verificarEmail(req.email(), req.codigo());
        String mensagem = verificado
                ? "E-mail confirmado. Agora você já pode entrar."
                : "Código inválido, expirado ou com tentativas esgotadas. Solicite um novo código.";
        return java.util.Map.of("verificado", verificado, "mensagem", mensagem);
    }

    @PostMapping("/reenviar-verificacao")
    public java.util.Map<String, String> reenviarVerificacao(
            @Valid @RequestBody ReenviarVerificacaoRequest req) {
        authService.reenviarVerificacao(req.email());
        return java.util.Map.of("mensagem",
            "Se houver uma conta Gmail aguardando confirmação e já tiver passado o intervalo, enviaremos outro código. Caso contrário, aguarde até 5 minutos antes de tentar novamente.");
    }

    @GetMapping("/eu")
    public UsuarioResponse eu(@AuthenticationPrincipal Usuario usuario) {
        return UsuarioResponse.de(usuario);
    }
}
