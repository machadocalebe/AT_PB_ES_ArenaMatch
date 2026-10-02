package br.infnet.arenamatch.reservas;

import br.infnet.arenamatch.config.RabbitMQConfig;
import br.infnet.arenamatch.eventos.ReservaCriadaEvent;
import br.infnet.arenamatch.quadras.Quadra;
import br.infnet.arenamatch.quadras.QuadraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - Testes Unitários")
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private QuadraRepository quadraRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ReservaService reservaService;

    private Quadra quadraDisponivel;
    private Quadra quadraEmManutencao;

    @BeforeEach
    void setUp() {
        quadraDisponivel = new Quadra("Quadra Ok", 100.0);
        quadraDisponivel.setId(1L);
        quadraDisponivel.setEmManutencao(false);

        quadraEmManutencao = new Quadra("Quadra Fechada", 100.0);
        quadraEmManutencao.setId(2L);
        quadraEmManutencao.setEmManutencao(true);
    }

    @Test
    @DisplayName("Deve criar reserva com sucesso e publicar evento no RabbitMQ")
    void deveCriarReservaEPublicarEvento() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setNomeLocatario("Ana Paula");
        dto.setQuadraId(1L);
        dto.setDataHoraInicio(LocalDateTime.of(2026, 11, 1, 10, 0));

        Reserva reservaSalva = new Reserva();
        reservaSalva.setId(10L);
        reservaSalva.setNomeLocatario("Ana Paula");
        reservaSalva.setDataHoraInicio(dto.getDataHoraInicio());
        reservaSalva.setQuadra(quadraDisponivel);

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraDisponivel));
        when(reservaRepository.existsByQuadraIdAndDataHoraInicio(any(), any())).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        Reserva resultado = reservaService.criarReserva(dto);

        assertNotNull(resultado);
        assertEquals("Ana Paula", resultado.getNomeLocatario());

        // Verifica que evento foi publicado corretamente na fila
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.ROUTING_KEY_RESERVA),
                any(ReservaCriadaEvent.class)
        );
    }

    @Test
    @DisplayName("Deve lancar BAD_REQUEST ao tentar reservar quadra em manutencao")
    void deveLancarExcecaoParaQuadraEmManutencao() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setNomeLocatario("Joao");
        dto.setQuadraId(2L);
        dto.setDataHoraInicio(LocalDateTime.now());

        when(quadraRepository.findById(2L)).thenReturn(Optional.of(quadraEmManutencao));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> reservaService.criarReserva(dto));

        assertTrue(ex.getMessage().contains("manutencao") || ex.getStatusCode().value() == 400);

        // Nenhum evento deve ser publicado
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Deve lancar CONFLICT ao tentar reservar horario ja ocupado")
    void deveLancarExcecaoParaHorarioOcupado() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setNomeLocatario("Carlos");
        dto.setQuadraId(1L);
        dto.setDataHoraInicio(LocalDateTime.of(2026, 11, 1, 10, 0));

        when(quadraRepository.findById(1L)).thenReturn(Optional.of(quadraDisponivel));
        when(reservaRepository.existsByQuadraIdAndDataHoraInicio(any(), any())).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> reservaService.criarReserva(dto));

        // Nenhum evento deve ser publicado em caso de conflito
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Deve lancar NOT_FOUND ao tentar deletar reserva inexistente")
    void deveLancarExcecaoAoDeletarReservaInexistente() {
        when(reservaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> reservaService.deletar(99L));
    }
}
