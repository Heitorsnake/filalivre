package com.filalivre.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.filalivre.dto.EntrarMercadoRequest;
import com.filalivre.model.Perfil;
import com.filalivre.model.Usuario;
import com.filalivre.repository.MercadoRepository;
import com.filalivre.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class MercadoServiceInputTest {

    @Mock
    private MercadoRepository mercadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MercadoService mercadoService;

    @Test
    void codigoSqlLikeNaoEncontraMercadoNemAlteraOperador() {
        String codigo = "' OR '1'='1";
        Usuario operador = Mockito.mock(Usuario.class);
        when(operador.getPerfil()).thenReturn(Perfil.OPERADOR);
        when(mercadoRepository.findByCodigoAcessoIgnoreCase(codigo)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mercadoService.entrar(new EntrarMercadoRequest(codigo), operador));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(mercadoRepository).findByCodigoAcessoIgnoreCase(codigo);
        verify(usuarioRepository, never()).save(operador);
    }
}
