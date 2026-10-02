package br.infnet.arenamatch.quadras;

import br.infnet.arenamatch.config.RabbitMQConfig;
import br.infnet.arenamatch.eventos.QuadraEmManutencaoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuadraService - Testes Unitários")
class QuadraServiceTest {

    @Mock
    private QuadraRepository quadraRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private QuadraService quadraService;

    private Quadra quadraExemplo;

    @BeforeEach
    void setUp() {
        quadraExemplo = new Quadra("Quadra Teste", 120.0);
        quadraExemplo.setId(1L);
        quadraExemplo.setEmManutencao(false);
    }

    @Test
    @DisplayName("Deve criar quadra com status de manutencao desativado por padrao")
    void deveCriarQuadra() {
        QuadraRequestDTO dto = new QuadraRequestDTO();
        dto.setNome("Nova Quadra");
        dto.setPrecoHora(150.0);

        Quadra quadraSalva = new Quadra("Nova Quadra", 150.0);
        quadraSalva.setId(99L);
        when(quadraRepository.save(any(Quadra.class))).thenReturn(quadraSalva);

        Quadra resultado = quadraService.criar(dto);

        assertNotNull(resultado);
        assertEquals("Nova Quadra", resultado.getNome());
        assertFalse(resultado.isEmManutencao());
        verify(quadraRepository, times(1)).save(any(Quadra.class));
    }

    @Test
    @DisplayName("Deve listar todas as quadras")
    void deveListarTodasQuadras() {
        when(quadraRepository.findAll()).thenReturn(List.of(quadraExemplo));

        List<Quadra> resultado = quadraService.listarTodas();

        assertEquals(1, resultado.size());
        verify(quadraRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar buscar quadra inexistente")
    void deveLancarExcecaoQuandoQuadraNaoEncontrada() {
        when(quadraRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> quadraService.atualizar(99L, new QuadraRequestDTO()));
    }

    @Test
    @DisplayName("Deve publicar evento no RabbitMQ ao colocar quadra em manutencao")
    void devePublicarEventoQuandoQuadraEntraEmManutencao() {
        quadraExemplo.setEmManutencao(false);
        Quadra quadraEmManutencao = new Quadra("Quadra Teste", 120.0);
        quadraEmManutencao.setId(1L);
        quadraEmManutencao.setEmManutencao(true);

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraExemplo));
        when(quadraRepository.save(any())).thenReturn(quadraEmManutencao);

        quadraService.alternarManutencao(1L);

        // Verifica que o evento foi publicado no RabbitMQ
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.ROUTING_KEY_QUADRA),
                any(QuadraEmManutencaoEvent.class)
        );
    }

    @Test
    @DisplayName("NAO deve publicar evento ao tirar quadra da manutencao")
    void naoDevePublicarEventoQuandoQuadraSaiDeManutencao() {
        quadraExemplo.setEmManutencao(true);
        Quadra quadraLiberada = new Quadra("Quadra Teste", 120.0);
        quadraLiberada.setId(1L);
        quadraLiberada.setEmManutencao(false);

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraExemplo));
        when(quadraRepository.save(any())).thenReturn(quadraLiberada);

        quadraService.alternarManutencao(1L);

        // Evento NAO deve ser publicado quando quadra sai da manutencao
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }
}
