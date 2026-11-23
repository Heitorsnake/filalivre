package com.filalivre.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.filalivre.model.Mercado;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DadosIniciaisTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CaixaRepository caixaRepository;

    @Mock
    private MercadoRepository mercadoRepository;

    @Test
    void restauraCredenciaisDasContasDemoJaExistentes() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Usuario admin = usuarioExistente("admin@filalivre.com", Perfil.OPERADOR, passwordEncoder);
        Usuario supervisor = usuarioExistente("supervisor@filalivre.com", Perfil.OPERADOR, passwordEncoder);
        Usuario operador = usuarioExistente("operador@filalivre.com", Perfil.SUPERVISOR, passwordEncoder);
        Mercado mercado = new Mercado();

        when(usuarioRepository.findByEmailIgnoreCase("admin@filalivre.com")).thenReturn(Optional.of(admin));
        when(usuarioRepository.findByEmailIgnoreCase("supervisor@filalivre.com"))
                .thenReturn(Optional.of(supervisor));
        when(usuarioRepository.findByEmailIgnoreCase("operador@filalivre.com")).thenReturn(Optional.of(operador));
        when(mercadoRepository.findByCodigoAcessoIgnoreCase("MERCADO1")).thenReturn(Optional.of(mercado));
        when(caixaRepository.count()).thenReturn(1L);

        CommandLineRunner runner = new DadosIniciais().criarDadosIniciais(
                usuarioRepository, caixaRepository, mercadoRepository, passwordEncoder, true, "", "");
        runner.run();

        assertContaDemo(admin, "admin123", Perfil.ADMINISTRADOR, passwordEncoder);
        assertContaDemo(supervisor, "supervisor123", Perfil.SUPERVISOR, passwordEncoder);
        assertContaDemo(operador, "operador123", Perfil.OPERADOR, passwordEncoder);
        assertEquals(mercado, supervisor.getMercado());
        assertEquals(mercado, operador.getMercado());
    }

    private Usuario usuarioExistente(String email, Perfil perfil, BCryptPasswordEncoder passwordEncoder) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNome("Nome antigo");
        usuario.setSenha(passwordEncoder.encode("senha-antiga"));
        usuario.setPerfil(perfil);
        usuario.setAtivo(false);
        return usuario;
    }

    private void assertContaDemo(Usuario usuario, String senha, Perfil perfil,
                                 BCryptPasswordEncoder passwordEncoder) {
        assertTrue(passwordEncoder.matches(senha, usuario.getSenha()));
        assertEquals(perfil, usuario.getPerfil());
        assertTrue(usuario.isAtivo());
    }
}
