package br.infnet.arenamatch.reservas;

import br.infnet.arenamatch.quadras.Quadra;
import br.infnet.arenamatch.quadras.QuadraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private QuadraRepository quadraRepository;

    @Test
    @DisplayName("Deve retornar TRUE se a quadra já estiver reservada naquele exato horário")
    void deveRetornarTrueSeHorarioOcupado() {
        // Preparação do cenário (Arrange)
        Quadra quadra = quadraRepository.save(new Quadra("Quadra Reserva", 100.0));
        LocalDateTime horario = LocalDateTime.of(2026, 8, 15, 18, 0);

        Reserva reserva = new Reserva();
        reserva.setNomeLocatario("Carlos");
        reserva.setDataHoraInicio(horario);
        reserva.setQuadra(quadra);
        reservaRepository.save(reserva);

        // Execução (Act)
        boolean ocupado = reservaRepository.existsByQuadraIdAndDataHoraInicio(quadra.getId(), horario);

        // Validação (Assert)
        assertTrue(ocupado, "A query deve detectar que o horário já está em uso");
    }

    @Test
    @DisplayName("Deve retornar FALSE se a quadra estiver livre no horário requisitado")
    void deveRetornarFalseSeHorarioLivre() {
        // Preparação do cenário (Arrange)
        Quadra quadra = quadraRepository.save(new Quadra("Quadra Livre", 100.0));
        LocalDateTime horarioDaReservaReal = LocalDateTime.of(2026, 8, 15, 18, 0);
        LocalDateTime horarioDaBuscaLivre = LocalDateTime.of(2026, 8, 15, 19, 0); // Uma hora depois

        Reserva reserva = new Reserva();
        reserva.setNomeLocatario("Maria");
        reserva.setDataHoraInicio(horarioDaReservaReal);
        reserva.setQuadra(quadra);
        reservaRepository.save(reserva);

        // Execução (Act)
        boolean ocupado = reservaRepository.existsByQuadraIdAndDataHoraInicio(quadra.getId(), horarioDaBuscaLivre);

        // Validação (Assert)
        assertFalse(ocupado, "A query não deve acusar conflito para horários diferentes");
    }
}