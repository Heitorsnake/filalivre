package com.filalivre.config;

import com.filalivre.model.Caixa;
import com.filalivre.model.Mercado;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner criarDadosIniciais(UsuarioRepository usuarioRepository,
                                         CaixaRepository caixaRepository,
                                         MercadoRepository mercadoRepository,
                                         PasswordEncoder passwordEncoder,
                                         @Value("${filalivre.dados-iniciais.demo:false}") boolean dadosDemo,
                                         @Value("${filalivre.admin.email:}") String emailAdmin,
                                         @Value("${filalivre.admin.password:}") String senhaAdmin) {
        return args -> {
            Mercado mercadoInicializacao = null;
            if (dadosDemo) {
                mercadoInicializacao = criarContasDemo(usuarioRepository, mercadoRepository, passwordEncoder);
            } else if (usuarioRepository.count() == 0) {
                if (!emailAdmin.isBlank() || !senhaAdmin.isBlank()) {
                    if (emailAdmin.isBlank() || senhaAdmin.length() < 12) {
                        throw new IllegalStateException(
                                "Defina FILALIVRE_ADMIN_EMAIL e FILALIVRE_ADMIN_PASSWORD com no mínimo 12 caracteres.");
                    }
                    usuarioRepository.save(usuario("Administrador", emailAdmin.trim(), senhaAdmin,
                            Perfil.ADMINISTRADOR, passwordEncoder));
                }
            }
            if (caixaRepository.count() == 0) {
                Mercado mercado = mercadoInicializacao != null
                        ? mercadoInicializacao
                        : mercadoRepository.findAll().stream().findFirst().orElse(null);
                if (mercado != null) {
                    for (int numero = 1; numero <= 6; numero++) {
                        Caixa caixa = new Caixa();
                        caixa.setNumero(numero);
                        caixa.setMercado(mercado);
                        caixaRepository.save(caixa);
                    }
                }
            }
        };
    }

    private Mercado criarContasDemo(UsuarioRepository usuarioRepository,
                                    MercadoRepository mercadoRepository,
                                    PasswordEncoder passwordEncoder) {
        Usuario admin = usuarioDemo(usuarioRepository, passwordEncoder, "Administrador",
                "admin@filalivre.com", "admin123", Perfil.ADMINISTRADOR);
        Usuario supervisor = usuarioDemo(usuarioRepository, passwordEncoder, "Supervisor Demonstração",
                "supervisor@filalivre.com", "supervisor123", Perfil.SUPERVISOR);
        Usuario operador = usuarioDemo(usuarioRepository, passwordEncoder, "Operador Demonstração",
                "operador@filalivre.com", "operador123", Perfil.OPERADOR);
        usuarioRepository.saveAll(List.of(admin, supervisor, operador));

        Mercado mercado = mercadoRepository.findByCodigoAcessoIgnoreCase("MERCADO1")
                .orElseGet(Mercado::new);
        mercado.setNome("Mercado Demonstração");
        mercado.setCodigoAcesso("MERCADO1");
        mercado.setGestor(supervisor);
        mercadoRepository.save(mercado);

        supervisor.setMercado(mercado);
        operador.setMercado(mercado);
        usuarioRepository.saveAll(List.of(supervisor, operador));
        return mercado;
    }

    private Usuario usuarioDemo(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                String nome, String email, String senha, Perfil perfil) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> usuario(nome, email, senha, perfil, passwordEncoder));
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setPerfil(perfil);
        usuario.setAtivo(true);
        if (usuario.getSenha() == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(senha));
        }
        return usuario;
    }

    private Usuario usuario(String nome, String email, String senha, Perfil perfil, PasswordEncoder encoder) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(encoder.encode(senha));
        u.setPerfil(perfil);
        return u;
    }
}
