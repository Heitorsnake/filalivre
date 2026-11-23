package com.filalivre.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.filalivre.model.Caixa;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.CaixaRepository;
import com.filalivre.repository.RegistroAcaoRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HistoricoControllerTest {

    @Mock
    private RegistroAcaoRepository registroAcaoRepository;

    @Mock
    private CaixaRepository caixaRepository;

    private HistoricoController controller;

    @BeforeEach
    void setUp() {
        controller = new HistoricoController(registroAcaoRepository, caixaRepository);
    }

    @Test
    void supervisorConsultaApenasCaixasDoProprioMercado() {
        Usuario supervisor = Mockito.mock(Usuario.class);
        Caixa caixaUm = Mockito.mock(Caixa.class);
        Caixa caixaDois = Mockito.mock(Caixa.class);
        when(supervisor.getPerfil()).thenReturn(Perfil.SUPERVISOR);
        when(supervisor.getMercadoId()).thenReturn(7L);
        when(caixaRepository.findAllByMercadoId(7L)).thenReturn(List.of(caixaUm, caixaDois));
        when(caixaUm.getNumero()).thenReturn(1);
        when(caixaDois.getNumero()).thenReturn(2);
        when(registroAcaoRepository.findTop100ByCaixaNumeroInOrderByMomentoDesc(List.of(1, 2)))
                .thenReturn(List.of());

        assertTrue(controller.listar(supervisor).isEmpty());

        verify(caixaRepository).findAllByMercadoId(7L);
        verify(registroAcaoRepository).findTop100ByCaixaNumeroInOrderByMomentoDesc(List.of(1, 2));
        verify(registroAcaoRepository, never()).findTop100ByOrderByMomentoDesc();
    }

    @Test
    void supervisorSemMercadoNaoRecebeHistoricoGlobal() {
        Usuario supervisor = Mockito.mock(Usuario.class);
        when(supervisor.getPerfil()).thenReturn(Perfil.SUPERVISOR);
        when(supervisor.getMercadoId()).thenReturn(null);

        assertTrue(controller.listar(supervisor).isEmpty());

        verifyNoInteractions(caixaRepository, registroAcaoRepository);
    }

    @Test
    void supervisorSemCaixasNaoRecorreAoHistoricoGlobal() {
        Usuario supervisor = Mockito.mock(Usuario.class);
        when(supervisor.getPerfil()).thenReturn(Perfil.SUPERVISOR);
        when(supervisor.getMercadoId()).thenReturn(7L);
        when(caixaRepository.findAllByMercadoId(7L)).thenReturn(List.of());

        assertTrue(controller.listar(supervisor).isEmpty());

        verify(caixaRepository).findAllByMercadoId(7L);
        verifyNoInteractions(registroAcaoRepository);
    }

    @Test
    void administradorMantemConsultaGlobalExistente() {
        Usuario administrador = Mockito.mock(Usuario.class);
        when(administrador.getPerfil()).thenReturn(Perfil.ADMINISTRADOR);
        when(registroAcaoRepository.findTop100ByOrderByMomentoDesc()).thenReturn(List.of());

        assertTrue(controller.listar(administrador).isEmpty());

        verify(registroAcaoRepository).findTop100ByOrderByMomentoDesc();
        verifyNoInteractions(caixaRepository);
    }
}
