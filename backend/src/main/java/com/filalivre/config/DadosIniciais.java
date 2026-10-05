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
            if (usuarioRepository.count() == 0) {
                if (dadosDemo) {
                    criarContasDemo(usuarioRepository, mercadoRepository, passwordEncoder);
                } else if (!emailAdmin.isBlank() || !senhaAdmin.isBlank()) {
                    if (emailAdmin.isBlank() || senhaAdmin.length() < 12) {
                        throw new IllegalStateException(
                                "Defina FILALIVRE_ADMIN_EMAIL e FILALIVRE_ADMIN_PASSWORD com no mínimo 12 caracteres.");
                    }
                    usuarioRepository.save(usuario("Administrador", emailAdmin.trim(), senhaAdmin,
                            Perfil.ADMINISTRADOR, passwordEncoder));
                }
            }
            if (caixaRepository.count() == 0) {
                Mercado mercado = mercadoRepository.findAll().stream().findFirst().orElse(null);
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

    private void criarContasDemo(UsuarioRepository usuarioRepository,
                                 MercadoRepository mercadoRepository,
                                 PasswordEncoder passwordEncoder) {
        Usuario admin = usuario("Administrador", "admin@filalivre.com", "admin123", Perfil.ADMINISTRADOR, passwordEncoder);
        Usuario supervisor = usuario("Supervisor Demonstração", "supervisor@filalivre.com", "supervisor123", Perfil.SUPERVISOR, passwordEncoder);
        Usuario operador = usuario("Operador Demonstração", "operador@filalivre.com", "operador123", Perfil.OPERADOR, passwordEncoder);
        usuarioRepository.saveAll(List.of(admin, supervisor, operador));

        Mercado mercado = new Mercado();
        mercado.setNome("Mercado Demonstração");
        mercado.setCodigoAcesso("MERCADO1");
        mercado.setGestor(supervisor);
        mercado = mercadoRepository.save(mercado);
        supervisor.setMercado(mercado);
        operador.setMercado(mercado);
        usuarioRepository.saveAll(List.of(supervisor, operador));
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
