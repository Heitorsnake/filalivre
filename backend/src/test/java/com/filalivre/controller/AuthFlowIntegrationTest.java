package com.filalivre.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filalivre.dto.CadastroRequest;
import com.filalivre.dto.LoginRequest;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(properties = "filalivre.dados-iniciais.demo=true")
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {

    private static final Path DATABASE = Path.of(System.getProperty("java.io.tmpdir"),
            "filalivre-auth-flow-" + UUID.randomUUID() + ".db");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private CaixaRepository caixaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:sqlite:" + DATABASE);
    }

    @Test
    void cadastroCriaContaPersistenteEIniciaSessao() throws Exception {
        String email = "novo-" + UUID.randomUUID() + "@exemplo.com";
        String senha = "SenhaSegura123!";
        CadastroRequest cadastro = new CadastroRequest("Novo Operador", email, senha, "operador");

        MvcResult resultado = mockMvc.perform(post("/api/auth/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.email").value(email))
                .andExpect(jsonPath("$.usuario.perfil").value("OPERADOR"))
                .andReturn();

        Usuario salvo = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow();
        assertTrue(passwordEncoder.matches(senha, salvo.getSenha()));
        MockHttpSession sessao = (MockHttpSession) resultado.getRequest().getSession(false);
        assertNotNull(sessao);

        mockMvc.perform(get("/api/auth/eu").session(sessao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void contasDemonstrativasPodemAutenticarEEstaoAssociadasAoMercado() throws Exception {
        Map<String, String> contas = Map.of(
                "admin@filalivre.com", "admin123",
                "supervisor@filalivre.com", "supervisor123",
                "operador@filalivre.com", "operador123");

        for (Map.Entry<String, String> conta : contas.entrySet()) {
            mockMvc.perform(post("/api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new LoginRequest(conta.getKey(), conta.getValue()))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(conta.getKey()));
        }

        Usuario supervisor = usuarioRepository.findByEmailIgnoreCase("supervisor@filalivre.com").orElseThrow();
        Usuario operador = usuarioRepository.findByEmailIgnoreCase("operador@filalivre.com").orElseThrow();
        var mercado = mercadoRepository.findByCodigoAcessoIgnoreCase("MERCADO1").orElseThrow();
        assertTrue(supervisor.isEnabled());
        assertTrue(operador.isEnabled());
        assertTrue(supervisor.pertenceAoMercado(mercado));
        assertTrue(operador.pertenceAoMercado(mercado));
        org.junit.jupiter.api.Assertions.assertEquals(Perfil.SUPERVISOR, supervisor.getPerfil());
        org.junit.jupiter.api.Assertions.assertEquals(Perfil.OPERADOR, operador.getPerfil());
        org.junit.jupiter.api.Assertions.assertEquals(6, caixaRepository.count());
    }

    @Test
    void cadastroRejeitaPerfilPrivilegiadoEEmailDuplicado() throws Exception {
        String email = "cadastro-" + UUID.randomUUID() + "@exemplo.com";
        CadastroRequest cadastro = new CadastroRequest("Operador", email, "SenhaSegura123!", "OPERADOR");

        mockMvc.perform(post("/api/auth/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastro)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastro)))
                .andExpect(status().isConflict());

        CadastroRequest cadastroAdmin = new CadastroRequest(
                "Administrador indevido", "admin-" + email, "SenhaSegura123!", "ADMINISTRADOR");
        mockMvc.perform(post("/api/auth/cadastro")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroAdmin)))
                .andExpect(status().isBadRequest());
    }
}
