package com.filalivre.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import com.filalivre.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailVerificacaoService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Duration INTERVALO_REENVIO = Duration.ofMinutes(5);
    private final ConcurrentHashMap<String, Instant> ultimosEnvios = new ConcurrentHashMap<>();

    private final JavaMailSender mailSender;
    private final String remetente;
    private final String senhaSmtp;
    private final String urlFrontend;

    public EmailVerificacaoService(JavaMailSender mailSender,
                                   @Value("${spring.mail.username:}") String remetente,
                                   @Value("${spring.mail.password:}") String senhaSmtp,
                                   @Value("${filalivre.email.frontend-url:http://localhost:8080/index.html}") String urlFrontend) {
        this.mailSender = mailSender;
        this.remetente = remetente;
        this.senhaSmtp = senhaSmtp;
        this.urlFrontend = urlFrontend;
    }

    public String gerarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean requerVerificacao(String email) {
        return email.toLowerCase(Locale.ROOT).endsWith("@gmail.com");
    }

    public String prepararToken(Usuario usuario) {
        String token = gerarToken();
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacaoEmailHash(hashToken(token));
        usuario.setTokenVerificacaoEmailExpiraEm(Instant.now().plus(Duration.ofHours(24)));
        return token;
    }

    public boolean reservarReenvio(String email) {
        String chave = email.toLowerCase(Locale.ROOT);
        Instant agora = Instant.now();
        AtomicBoolean reservado = new AtomicBoolean();
        ultimosEnvios.compute(chave, (ignorada, ultimoEnvio) -> {
            if (ultimoEnvio == null || !ultimoEnvio.plus(INTERVALO_REENVIO).isAfter(agora)) {
                reservado.set(true);
                return agora;
            }
            return ultimoEnvio;
        });
        return reservado.get();
    }

    public String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 não está disponível", ex);
        }
    }

    public void enviar(String nome, String email, String token) {
        if (remetente.isBlank() || senhaSmtp.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "O envio de e-mail ainda não está configurado.");
        }

        String link = urlFrontend + "#verificar-email=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(email);
        mensagem.setSubject("Confirme seu e-mail — FilaLivre");
        mensagem.setText("Olá, " + nome + "!\n\n"
                + "Para confirmar seu endereço de e-mail e ativar sua conta no FilaLivre, acesse o link abaixo.\n\n"
                + link + "\n\n"
                + "O link expira em 24 horas. Se você não criou esta conta, ignore esta mensagem.");
        try {
            mailSender.send(mensagem);
            ultimosEnvios.put(email.toLowerCase(Locale.ROOT), Instant.now());
        } catch (org.springframework.mail.MailException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Não foi possível enviar o e-mail de confirmação. Tente novamente mais tarde.", ex);
        }
    }
}