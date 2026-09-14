package com.filalivre.config;

import com.filalivre.model.Caixa;
import com.filalivre.model.Mercado;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.util.List;
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
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = usuario("Administrador", "admin@filalivre.com", "admin123", Perfil.ADMINISTRADOR, passwordEncoder);
                Usuario gerente = usuario("Gestor Demonstração", "gestor@filalivre.com", "gestor123", Perfil.GERENTE, passwordEncoder);
                Usuario operador = usuario("Operador Demonstração", "operador@filalivre.com", "operador123", Perfil.OPERADOR, passwordEncoder);
                usuarioRepository.saveAll(List.of(admin, gerente, operador));

                Mercado mercado = new Mercado();
                mercado.setNome("Mercado Demonstração");
                mercado.setCodigoAcesso("MERCADO1");
                mercado.setGestor(gerente);
                mercado = mercadoRepository.save(mercado);
                gerente.setMercado(mercado);
                operador.setMercado(mercado);
                usuarioRepository.saveAll(List.of(gerente, operador));
            }
            if (caixaRepository.count() == 0) {
                Mercado mercado = mercadoRepository.findAll().stream().findFirst().orElse(null);
                for (int numero = 1; numero <= 6; numero++) {
                    Caixa caixa = new Caixa();
                    caixa.setNumero(numero);
                    caixa.setMercado(mercado);
                    caixaRepository.save(caixa);
                }
            }
        };
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
