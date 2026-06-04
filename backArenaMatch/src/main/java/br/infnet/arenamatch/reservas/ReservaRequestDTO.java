package br.infnet.arenamatch.reservas;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaRequestDTO {
    private String nomeLocatario;
    private Long quadraId;
    private LocalDateTime dataHoraInicio;
}