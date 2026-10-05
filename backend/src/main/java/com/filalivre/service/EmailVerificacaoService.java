package com.filalivre.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    public EmailVerificacaoService(JavaMailSender mailSender,
                                   @Value("${spring.mail.username:}") String remetente,
                                   @Value("${spring.mail.password:}") String senhaSmtp) {
        this.mailSender = mailSender;
        this.remetente = remetente;
        this.senhaSmtp = senhaSmtp;
    }

    public String gerarToken() {
        return String.format(Locale.ROOT, "%06d", RANDOM.nextInt(1_000_000));
    }

    public boolean requerVerificacao(String email) {
        return email.toLowerCase(Locale.ROOT).endsWith("@gmail.com");
    }

    public String prepararToken(Usuario usuario) {
        String token = gerarToken();
        usuario.setEmailVerificado(false);
        usuario.setTokenVerificacaoEmailHash(hashToken(token));
        usuario.setTokenVerificacaoEmailExpiraEm(Instant.now().plus(Duration.ofMinutes(10)));
        usuario.setTentativasVerificacaoEmail(0);
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
                "A confirmação de Gmail está indisponível: o administrador precisa configurar "
                    + "FILALIVRE_SMTP_USERNAME e FILALIVRE_SMTP_PASSWORD no servidor.");
        }

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(email);
        mensagem.setSubject("Código de verificação — FilaLivre");
        mensagem.setText("Olá, " + nome + "!\n\n"
            + "Digite este código na tela de criação da conta para confirmar seu Gmail:\n\n"
            + token + "\n\n"
            + "O código expira em 10 minutos. Se você não criou esta conta, ignore esta mensagem.");
        try {
            mailSender.send(mensagem);
            ultimosEnvios.put(email.toLowerCase(Locale.ROOT), Instant.now());
        } catch (org.springframework.mail.MailException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Não foi possível enviar o e-mail de confirmação. Tente novamente mais tarde.", ex);
        }
    }
}