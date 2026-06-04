package br.infnet.arenamatch.reservas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByQuadraIdAndDataHoraInicio(Long quadraId, LocalDateTime dataHoraInicio);
}